package com.tingxuelou.www.provider.utils;

/**
 * 断言工具类
 * <p>
 * Date: 2020/9/8 下午3:20
 * Copyright (C), 2015-2020
 */
public class Assert {
    public static void notNull(Object obj, String message){
        if (obj == null){
            throw new RuntimeException(message);
        }
    }

}
