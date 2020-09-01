package com.tingxuelou.www.provider.mq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 无序消息listener 抽象类
 * <p>
 * Date: 2020/8/28 下午3:16
 * Copyright (C), 2015-2020
 */
public abstract class AbstractCurrentlyListener extends AbstractListener implements MessageListenerConcurrently {
    private static final Logger log = LoggerFactory.getLogger(AbstractCurrentlyListener.class);

    private static final String CONSUMED_CURRENTLY_KEY = "ROCKET_MQ_CONSUMER_CURRENTLY_";

    @Override
    public String getMessageCacheKey(MessageExt msg) {
        return CONSUMED_CURRENTLY_KEY + msg.getTopic() + msg.getTags() + msg.getKeys();
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        log.info("receive rocketmq msg");
        boolean result = true;
        for (MessageExt msg : msgs) {
            result = result & consumeSingleMessage(msg);
        }
        return result ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
