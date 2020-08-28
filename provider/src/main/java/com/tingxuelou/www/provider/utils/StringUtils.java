package com.tingxuelou.www.provider.utils;

/**
 * 字符串工具类
 * <p>
 * Date: 2020-08-02 16:00
 * Copyright (C), 2015-2020
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param con 字符串
     * @return boolean
     */
    public static boolean isEmpty(String con){
        if (con == null || con.trim().length() < 1){
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param con 字符串
     * @return boolean
     */
    public static boolean isNotEmpty(String con){
        return !isEmpty(con);
    }
}
