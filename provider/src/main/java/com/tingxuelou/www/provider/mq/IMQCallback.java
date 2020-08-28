package com.tingxuelou.www.provider.mq;

import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 异步消息回调
 * <p>
 * Date: 2020/8/27 下午11:25
 * Copyright (C), 2015-2020
 */
public interface IMQCallback extends SendCallback, IRocketMQ {

}
