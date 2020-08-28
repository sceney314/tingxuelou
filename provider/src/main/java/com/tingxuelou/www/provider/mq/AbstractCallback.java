package com.tingxuelou.www.provider.mq;

import org.apache.rocketmq.client.producer.SendResult;

/**
 * MQ 回调抽象类
 * <p>
 * Date: 2020/8/27 下午11:49
 * Copyright (C), 2015-2020
 */
public abstract class AbstractCallback implements IMQCallback{
    // mq topic
    protected String topic;

    // mq tag
    protected String tag;

    public AbstractCallback(String topic, String tag) {
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
    public void onSuccess(SendResult result) {
        MQCallBackFactory.onSuccess(this, result);
    }

    @Override
    public void onException(Throwable e) {
        MQCallBackFactory.onException(this, e);
    }
}
