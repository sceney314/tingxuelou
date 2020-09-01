package com.tingxuelou.www.provider.utils;

import com.tingxuelou.www.provider.init.AbstractInit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;


/**
 * redis 工具类
 * <p>
 * Date: 2020-08-02 00:14
 * Copyright (C), 2015-2020
 */
@Component
public class RedisUtils extends AbstractInit {
    private static volatile JedisPool pool;

    private static final String HOST;
    private static final int PORT;
    static {
        String[] ip = PropertyUtils.getString("redis.host").split(":");
        HOST = ip[0];
        PORT = Integer.parseInt(ip[1]);

        getInstance();
    }

    @Override
    public void init(ApplicationContext context) {

    }

    @Override
    public void destroy(ApplicationContext context) {

    }

    /**
     * 获取缓存实例
     *
     * @return JedisPool
     */
    private static JedisPool getInstance(){
        if (pool == null){
            synchronized (RedisUtils.class){
                if(pool == null){
                    pool = new JedisPool(HOST, PORT);
                }
            }
        }

        return pool;
    }

    /**
     * 设置带过期的 key
     *
     * @param key 缓存key
     * @param val 缓存 value
     * @param expire 过期时间，单位：秒
     */
    public static void setWithExpire(String key, String val, Integer expire){
        SetParams params = new SetParams();
        params.ex(expire);
        getInstance().getResource().set(key, val, params);
    }

    /**
     * 根据 key 获取缓存
     *
     * @param key 缓存 key
     * @return String
     */
    public static String get(String key){
        return getInstance().getResource().get(key);
    }
}
