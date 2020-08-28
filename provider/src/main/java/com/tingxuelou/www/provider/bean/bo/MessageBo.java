package com.tingxuelou.www.provider.bean.bo;

import com.tingxuelou.www.provider.common.constants.biz.MQDelay;
import lombok.Data;

/**
 * 消息 messge
 * <p>
 * Date: 2020-08-04 11:07
 * Copyright (C), 2015-2020
 */
@Data
public class MessageBo<T>{

    // 用于保证同一个对象的消息发送到同一个队列中
    private long bizId;

    // 延迟等级
    private MQDelay delay = MQDelay.DELAY_0;

    // 消息 key，业务保证唯一
    private String key;

    // 过滤 tag
    private String tag;

    // 消息 topic
    private String Topic;

    // 消息对象
    private T t;
}
