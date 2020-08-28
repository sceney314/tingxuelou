package com.tingxuelou.www.provider.mq;

import com.tingxuelou.www.provider.common.constants.biz.TransactionStatus;
import com.tingxuelou.www.provider.service.business.mq.transaction.IMQBizTransaction;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * rocketMQ 事务消息 listener
 * <p>
 * Date: 2020-08-04 16:31
 * Copyright (C), 2015-2020
 */
@Component
public class MQTransactionFactory implements TransactionListener {
    private static final Logger log = LoggerFactory.getLogger(MQTransactionFactory.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private IMQBizTransaction[] transactions;

    // 事务节点，容量是 1W
    private static final LinkedBlockingQueue<Node> queue = new LinkedBlockingQueue<>(1_0000);

    // 事务节点
    private class Node {
        private static final long MIN_1 = 60_000L;
        // 状态
        TransactionStatus status;

        // 事务 ID
        String transId;

        // 事务开始时间
        long unix;

        public Node(TransactionStatus status, String transId, long unix) {
            this.status = status;
            this.transId = transId;
            this.unix = unix;
        }

        public void check() {
            if (System.currentTimeMillis() - this.unix < MIN_1) {
                return;
            }
            try {
                queue.remove(this);
                // 从 map 中删除
                localTrans.remove(this.transId);
            } catch (Exception e) {
                log.warn("删除节点异常", e);
            }
        }
    }

    /**
     * 事务状态容器
     */
    private final ConcurrentHashMap<String, Node> localTrans = new ConcurrentHashMap<>();

    // 事务容器
    private final ConcurrentHashMap<String, IMQBizTransaction> transactionMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        for (IMQBizTransaction transaction : transactions) {
            transactionMap.put(transaction.getTopicTag(), transaction);
        }

        // 添加定时清理任务
        taskScheduler.scheduleAtFixedRate(() -> {
            for (Node value : queue) {
                value.check();
            }
        }, 300_000);
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String key = msg.getTopic() + "-" + msg.getTags();
        // 不存在的直接返回回滚
        if (!transactionMap.containsKey(key)) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        // 先设置成未知
        TransactionStatus status = transactionMap.get(key).executeLocalTransaction(msg, arg);
        if (TransactionStatus.UNKNOW.equals(status)) {
            addNode(msg);
            return LocalTransactionState.UNKNOW;
        }
        if (TransactionStatus.COMMIT.equals(status)) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    private void addNode(Message msg) {
        Node node = new Node(TransactionStatus.UNKNOW, msg.getTransactionId(), System.currentTimeMillis());
        boolean add = false;
        try {
            // 加入任务队列成功
            for (int i = 0; i < 3; i++){
                if (queue.offer(node, 200L, TimeUnit.MILLISECONDS)) {
                    localTrans.put(msg.getTransactionId(), node);
                    add = true;
                } else {
                    taskExecutor.submit(() -> {
                        for (Node value : queue) {
                            value.check();
                        }
                    });
                    // 睡 100 毫秒
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }
        } catch (Exception e) {
            // 异常，确保 map 添加
            localTrans.put(msg.getTransactionId(), node);
            add = true;
            log.warn("添加节点异常", e);
        }

        // 任务队列满，任务数量达到 1W
        if (!add){
            // 阻塞添加(考虑拒绝策略)
            queue.offer(node);
            // 溢出添加
            localTrans.put(msg.getTransactionId(), node);
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Node node = localTrans.get(msg.getTransactionId());
        if (node == null || TransactionStatus.UNKNOW.equals(node.status)) {
            return LocalTransactionState.UNKNOW;
        }

        localTrans.get(msg.getTransactionId()).check();
        if (TransactionStatus.COMMIT.equals(node.status)) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
