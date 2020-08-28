package com.tingxuelou.www.provider.mq;

import org.apache.rocketmq.client.producer.TransactionListener;

/**
 * MQ 事务消息
 * <p>
 * Date: 2020/8/27 下午11:26
 * Copyright (C), 2015-2020
 */
public interface IMQTransaction extends TransactionListener, IRocketMQ {
}
