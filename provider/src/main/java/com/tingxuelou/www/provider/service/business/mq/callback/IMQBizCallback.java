package com.tingxuelou.www.provider.service.business.mq.callback;

import com.tingxuelou.www.provider.mq.MQCallBackFactory;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * MQ 回调具体事务处理
 * <p>
 * Date: 2020/8/27 下午11:54
 * Copyright (C), 2015-2020
 */
public interface IMQBizCallback {
    /**
     * 获取 MQ topic 和 tag
     *
     * @return String
     */
    String getTopicTag();

    /**
     * 成功
     *
     * @param result 结果
     */
    void onSuccess(SendResult result);

    /**
     * 失败
     *
     * @param e 异常对象
     */
    void onException(Throwable e);
}
