package com.tingxuelou.www.provider.utils;

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
public class RedisUtils {
    private static volatile JedisPool pool;

    private static final String HOST;
    private static final int PORT;
    static {
        String[] ip = PropertyUtils.getString("redis.host").split(":");
        HOST = ip[0];
        PORT = Integer.parseInt(ip[1]);

        getInstance();
    }

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

    public static void setWithExpire(String key, String val, Integer expire){
        SetParams params = new SetParams();
        params.ex(expire);
        pool.getResource().set(key, val, params);
    }

    public static String get(String key){
        return pool.getResource().get(key);
    }
}
