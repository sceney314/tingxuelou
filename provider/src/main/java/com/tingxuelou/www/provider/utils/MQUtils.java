package com.tingxuelou.www.provider.utils;

import com.alibaba.fastjson.JSON;
import com.tingxuelou.www.provider.bean.bo.MessageBo;
import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.common.constants.biz.MQDelay;
import com.tingxuelou.www.provider.exceptions.ServiceException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;


/**
 * RocketMQ 工具类
 * <p>
 * Date: 2020-08-04 10:49
 * Copyright (C), 2015-2020
 */
public class MQUtils {
    private static final Logger log = LoggerFactory.getLogger(MQUtils.class);

    // 普通消息、顺序消息、延迟消息、单向消息、批量消息 producer
    private static final DefaultMQProducer defaultMQProducer;

    // 事务消息 producer
    private static final TransactionMQProducer transactionMQProducer;

    // name server 地址
    private static final String NAME_SERVER = PropertyUtils.getString("rocketmq.nameServer");

    // 实例容器
    private static final ConcurrentHashMap<String, MQProducer> producerMap = new ConcurrentHashMap<>();

    private static final String DEFAULT_PRODUCER =  "P_" + TxlConst.APPLICATION + "_GROUP";
    private static final String TRANCATION_PRODUCER =  "P_" + TxlConst.APPLICATION + "_TRANSACTION_GROUP";

    // 有序消息，队列选择：确保同一个对象使用同一个队列
    private static final MessageQueueSelector seletor = (mqs, msg, arg) -> {
        long count = mqs.size();
        if (arg instanceof MessageBo){
            MessageBo<?> bo = (MessageBo<?>)arg;
            int idx = (int)(bo.getBizId() % count);
            return mqs.get(idx);
        }else{
            int idx = Math.abs(arg.hashCode()) % mqs.size();
            return mqs.get(idx);
        }
    };

    static {
        // 默认消息 producer
        defaultMQProducer = new DefaultMQProducer(DEFAULT_PRODUCER);
        defaultMQProducer.setNamespace(NAME_SERVER);
        defaultMQProducer.setInstanceName(DEFAULT_PRODUCER + "_WORKER_" + System.currentTimeMillis());
        producerMap.put(defaultMQProducer.getProducerGroup(), defaultMQProducer);

        // 事务消息 producer
        transactionMQProducer = new TransactionMQProducer(TRANCATION_PRODUCER);
        transactionMQProducer.setNamespace(NAME_SERVER);
        transactionMQProducer.setInstanceName(TRANCATION_PRODUCER + "_WORKER_" + System.currentTimeMillis());
        producerMap.put(transactionMQProducer.getProducerGroup(), transactionMQProducer);
        try {
            defaultMQProducer.start();
            transactionMQProducer.start();
        }catch (MQClientException e){
            log.error("mq 启动异常", e);
            throw new RuntimeException("mq 启动异常");
        }
    }

    private static boolean isSendOk(SendResult result){
        if (SendStatus.SEND_OK.equals(result.getSendStatus())){
            return true;
        }
        return false;
    }

    /**
     * 发送有序消息
     *
     * @param bo 消息对象
     * @return boolean
     */
    public static <T> boolean sendOrderMsg(MessageBo<T> bo){
        try{
            log.info("发送消息:{}", bo);
//            Message msg = new Message(bo.getTopic(), bo.getTag(), bo.getKey(), JSON.toJSONString(bo.getT()).getBytes(RemotingHelper.DEFAULT_CHARSET));
            Message msg = createMessage(bo);
            SendResult result = producerMap.get(DEFAULT_PRODUCER).send(msg, seletor, bo);
            log.info("发送结果:{}, ", result);
            return isSendOk(result);
        }catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e){
            log.warn("发送有序消息异常", e);
        }
        return false;
    }

    /**
     * 创建消息对象
     *
     * @param bo 消息对象
     * @return Message
     */
    private static <T> Message createMessage(MessageBo<T> bo){
        String content = JSON.toJSONString(bo.getT());
        try {
            Message msg = new Message(bo.getTopic(), bo.getTag(), bo.getKey(), content.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 如果延迟
            if (!MQDelay.DELAY_0.equals(bo.getDelay())){
                msg.setDelayTimeLevel(bo.getDelay().code);
            }
            return msg;
        }catch (UnsupportedEncodingException e){
            log.warn("生成 Message 消息对象异常", e);
            throw new ServiceException("生成 Message 消息对象异常");
        }
    }

    /**
     * 二进制解析成字符串
     *
     * @param bytes 二进制数据
     * @return String
     */
    public static String byte2String(byte[] bytes) {
        String obj = "";
        ObjectInputStream oi = null;
        ByteArrayInputStream bi = null;
        try {
            // bytearray to object
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            obj = (String) oi.readObject();
        } catch (Exception e) {
            log.warn("ByteToObject error!", e);
        } finally {
            if (bi != null) {
                try {
                    bi.close();
                } catch (Exception e) {
                    log.warn("关闭异常:", e);
                }
            }
            if (oi != null) {
                try {
                    oi.close();
                } catch (Exception e) {
                    log.warn("关闭异常:", e);
                }
            }
        }
        return obj;
    }




}
