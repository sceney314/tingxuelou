package com.tingxuelou.www.provider.bean.bo;

import lombok.Data;

/**
 * MQ 监听
 * <p>
 * Date: 2020-08-04 11:02
 * Copyright (C), 2015-2020
 */
@Data
public class MQListener {
    // topic
    private String Topic;

    private String Tags;

}
