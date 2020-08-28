package com.tingxuelou.www.provider.service.business.mq.callback;


import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

/**
 * 异步消息回调测试
 * <p>
 * Date: 2020/8/27 下午11:44
 * Copyright (C), 2015-2020
 */
@Component
public class AsynTestCallback extends AbstractMQBizCallback {
    @Override
    String getTopic() {
        return null;
    }

    @Override
    String getTag() {
        return null;
    }

    @Override
    public void onSuccess(SendResult result) {

    }

    @Override
    public void onException(Throwable e) {

    }
}
