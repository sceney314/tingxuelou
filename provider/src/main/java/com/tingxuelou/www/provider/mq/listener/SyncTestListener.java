package com.tingxuelou.www.provider.mq.listener;

import com.tingxuelou.www.provider.mq.AbstractCurrentlyListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 测试监听
 * <p>
 * Date: 2020/9/1 下午5:00
 * Copyright (C), 2015-2020
 */
public class SyncTestListener extends AbstractCurrentlyListener {
    private static final Logger log = LogManager.getLogger(SyncTestListener.class);

    @Override
    public boolean readMessage(String content) {
        log.info("收到消息:{}", content);
        return true;
    }
}
