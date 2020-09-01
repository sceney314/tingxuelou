package com.tingxuelou.www.provider.mq.listener;

import com.tingxuelou.www.provider.mq.AbstractCurrentlyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试监听
 * <p>
 * Date: 2020/9/1 下午5:00
 * Copyright (C), 2015-2020
 */
public class SyncTestListener extends AbstractCurrentlyListener {
    private static final Logger log = LoggerFactory.getLogger(SyncTestListener.class);

    @Override
    public boolean readMessage(String content) {
        log.info("收到消息:{}", content);
        return true;
    }
}
