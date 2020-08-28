package com.tingxuelou.www.provider.utils;

import lombok.Data;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置工具类
 * <p>
 * Date: 2020-08-02 15:35
 * Copyright (C), 2015-2020
 */
public class PropertyUtils {
    private static volatile Map<String, Entry> propertiesMap = new ConcurrentHashMap<>();

    private static final String APPLICATION_KEY = "application.name";

    static {
        refreshConfig();
    }

    /**
     * 初始化配置
     */
    private static void refreshConfig(){
        Map<String, Entry> map = new ConcurrentHashMap<>();
        init("config", map);

        String applicationName = map.containsKey(APPLICATION_KEY) ? map.get(APPLICATION_KEY).getValue() : "";
        if(StringUtils.isNotEmpty(applicationName)){
            init(applicationName, map);
        }

        propertiesMap = map;
    }

    private static void init(String baseName, Map<String, Entry> map){
        ResourceBundle rb = ResourceBundle.getBundle(baseName, Locale.getDefault());
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            map.put(key, new Entry(key, rb.getString(key)));
        }
    }

    public static String getString(String key){
        return getEntry(key).getValue();
    }

    public static int getIntString(String key){
        return Integer.parseInt(getEntry(key).getValue());
    }

    private static Entry getEntry(String key){
        Entry entry = propertiesMap.getOrDefault(key, null);
        if (entry == null){
            throw new RuntimeException("key:" + key + ", value is null");
        }

        return entry;
    }

    @Data
    private static class Entry{
        // 属性 key
        private String key;

        // 属性 value
        private String value;

        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
