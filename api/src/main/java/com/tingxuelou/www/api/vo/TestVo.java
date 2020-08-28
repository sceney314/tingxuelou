package com.tingxuelou.www.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Date: 2020-08-03 09:50
 * Copyright (C), 2015-2020
 */
@Data
public class TestVo implements Serializable {
    private String msg;

    public TestVo() {
    }

    public TestVo(String msg) {
        this.msg = msg;
    }
}
