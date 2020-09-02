package com.tingxuelou.www.provider.mq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 有序消息消费抽象类
 * <p>
 * Date: 2020/8/28 下午3:39
 * Copyright (C), 2015-2020
 */
public abstract class AbstractOrderlyListener extends AbstractListener implements MessageListenerOrderly {
    private static final Logger log = LogManager.getLogger(AbstractOrderlyListener.class);

    private static final String CONSUMED_ORDERLY_KEY = "ROCKET_MQ_CONSUMER_ORDERLY_";

    @Override
    public String getMessageCacheKey(MessageExt msg) {
        return CONSUMED_ORDERLY_KEY + msg.getTopic() + msg.getTags() + msg.getKeys();
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        log.info("receive rocketmq msg, msgs:{}, MessageQueue:{}", msgs, context.getMessageQueue());
        boolean result = true;
        for (MessageExt msg : msgs) {
            result = result & consumeSingleMessage(msg);
        }

        return result ? ConsumeOrderlyStatus.SUCCESS : ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }
}
