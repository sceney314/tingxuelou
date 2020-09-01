package com.tingxuelou.www.provider.mq;

import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.init.AbstractInit;
import com.tingxuelou.www.provider.utils.StringUtils;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mq消费工厂
 * <p>
 * Date: 2020/8/28 下午7:43
 * Copyright (C), 2015-2020
 */
@Data
public class MQConsumerFactory extends AbstractInit {
    @Override
    public void destroy(ApplicationContext context) {

    }

    private static final Logger log = LoggerFactory.getLogger(MQConsumerFactory.class);
    private volatile boolean hasListened = false;

    // 非有序小监听对象
    private List<AbstractListener> currentlyListeners = null;

    // 有序消息监听对象
    private List<AbstractListener> orderlyListeners = null;

    private static final ConcurrentHashMap<String, DefaultMQPushConsumer> consumerMap = new ConcurrentHashMap<>();

    private void registerListeners(List<AbstractListener> listeners) {
        for (AbstractListener listener : listeners) {
            try {
                String topic = listener.getTopic();
                String tags = listener.getTags();
                if (StringUtils.isEmpty(topic)) {
                    log.warn("rocketmq loading listener error! can not found topic:{}", topic);
                    continue;
                }
                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("S_" + TxlConst.APPLICATION + "_CG" + "_" + topic);
                consumer.setNamesrvAddr(TxlConst.NAME_SERVER);
                consumer.setInstanceName(TxlConst.APPLICATION + "Consumer_" + listener.getConsumerName() + "_" + System.currentTimeMillis());
                consumer.setMessageModel(MessageModel.CLUSTERING);
                consumer.subscribe(topic, tags);
                if (listener instanceof AbstractCurrentlyListener) {
                    consumer.registerMessageListener((AbstractCurrentlyListener) listener);
                } else if (listener instanceof AbstractOrderlyListener) {
                    consumer.registerMessageListener((AbstractOrderlyListener) listener);
                } else {
                    throw new RuntimeException("register listener error!");
                }
                consumerMap.put(topic, consumer);
                consumer.start();
                log.info("rocketmq loading listener:nameServerAddr:{},InstanceName:{},MessageModel:{},topic:{},listener:{}", consumer.getNamesrvAddr(), consumer.getInstanceName(), consumer.getMessageModel(), topic, listener.getClass().getSimpleName());
            } catch (Exception e) {
                log.warn("rocketmq loading listener error!", e);
                throw new RuntimeException("register listener error!");
            }
        }
    }

    public void init(ApplicationContext context) {
        if(!hasListened)  {
            log.info("rocketmq loading listener begin!");
            if (CollectionUtils.isNotEmpty(currentlyListeners)) {
                registerListeners(currentlyListeners);
            }

            if (CollectionUtils.isNotEmpty(orderlyListeners)) {
                registerListeners(orderlyListeners);
            }

            log.info("rocketmq loading listener end!");
            hasListened = true;
        }
    }
}
