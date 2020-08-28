package com.tingxuelou.www.provider.common.constants.biz;

import com.tingxuelou.www.provider.exceptions.ServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * mq 延迟枚举
 * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 * <p>
 * Date: 2020-08-06 11:42
 * Copyright (C), 2015-2020
 */
public enum MQDelay {
    DELAY_0(0, "没有延迟"),
    DELAY_1s(1, "1秒延迟"),
    DELAY_5s(2, "5秒延迟"),
    DELAY_10s(3, "10秒延迟"),
    DELAY_30s(4, "30秒延迟"),
    DELAY_1m(5, "1分钟延迟"),
    DELAY_2m(6, "2分钟延迟"),
    DELAY_3m(7, "3分钟延迟"),
    DELAY_4m(8, "4分钟延迟"),
    DELAY_5m(9, "5分钟延迟"),
    DELAY_6m(10, "6分钟延迟"),
    DELAY_7m(11, "7分钟延迟"),
    DELAY_8m(12, "8分钟延迟"),
    DELAY_9m(13, "9分钟延迟"),
    DELAY_10m(14, "10分钟延迟"),
    DELAY_20m(15, "20分钟延迟"),
    DELAY_30m(16, "30分钟延迟"),
    DELAY_1h(17, "1小时延迟"),
    DELAY_2h(18, "2小时延迟"),
    ;
    public final int code;
    public final String msg;
    private final static Map<Integer, MQDelay> map = new HashMap<>();
    static {
        for (MQDelay entry : values()){
            map.put(entry.code, entry);
        }
    }

    MQDelay(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static MQDelay getByCode(int code){
        if (!map.containsKey(code)){
            throw new ServiceException("code 非法");
        }

        return map.get(code);
    }
}
