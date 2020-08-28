package com.tingxuelou.www.provider.mq;

/**
 * rocketMQ
 * <p>
 * Date: 2020/8/28 上午12:14
 * Copyright (C), 2015-2020
 */
public interface IRocketMQ {
    /**
     * 获取 MQ topic
     *
     * @return String
     */
    String getTopic();

    /**
     * 获取 MQ tag
     *
     * @return String
     */
    String getTag();
}
