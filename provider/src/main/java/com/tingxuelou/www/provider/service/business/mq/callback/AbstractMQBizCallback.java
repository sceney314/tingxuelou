package com.tingxuelou.www.provider.service.business.mq.callback;

/**
 * mq 回调抽象业务类
 * <p>
 * Date: 2020/8/28 上午12:01
 * Copyright (C), 2015-2020
 */
public abstract class AbstractMQBizCallback implements IMQBizCallback{
    /**
     * 获取 MQ topic
     *
     * @return String
     */
    abstract String getTopic();

    /**
     * 获取 MQ tag
     *
     * @return String
     */
    abstract String getTag();

    @Override
    public String getTopicTag() {
        return getTopic() + '-' + getTag();
    }
}
