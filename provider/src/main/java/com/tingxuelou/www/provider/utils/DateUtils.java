package com.tingxuelou.www.provider.utils;

import com.tingxuelou.www.provider.common.constants.TxlConst;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类
 * <p>
 * Date: 2020/8/28 下午7:57
 * Copyright (C), 2015-2020
 */
public class DateUtils {
    private static final ConcurrentHashMap<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();


    public static String date2String(){
        return date2String(TxlConst.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 日期格式化
     *
     * @param pattern 格式化模式
     * @return String
     */
    public static String date2String(String pattern){
        return date2String(LocalDateTime.now(), pattern);
    }

    /**
     * 日期格式化
     *
     * @param pattern 格式化模式
     * @return String
     */
    public static String date2String(LocalDateTime localDateTime, String pattern){
        if (!formatterMap.containsKey(pattern)){
            formatterMap.put(pattern, DateTimeFormatter.ofPattern(pattern));
        }

        return localDateTime.format(formatterMap.get(pattern));
    }
}
