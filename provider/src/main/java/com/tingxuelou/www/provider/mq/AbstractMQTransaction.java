package com.tingxuelou.www.provider.mq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事务消息抽象类
 * <p>
 * Date: 2020/8/28 上午12:14
 * Copyright (C), 2015-2020
 */
public abstract class AbstractMQTransaction implements IMQTransaction{
    // mq topic
    protected String topic;

    // mq tag
    protected String tag;

    public AbstractMQTransaction(String topic, String tag) {
        this.topic = topic;
        this.tag = tag;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        return null;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return null;
    }
}
