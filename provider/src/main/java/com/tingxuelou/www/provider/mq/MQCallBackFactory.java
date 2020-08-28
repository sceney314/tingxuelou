package com.tingxuelou.www.provider.mq;

import com.tingxuelou.www.provider.exceptions.ServiceException;
import com.tingxuelou.www.provider.service.business.mq.callback.IMQBizCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步消息 MQ 回调
 * <p>
 * Date: 2020-08-04 15:14
 * Copyright (C), 2015-2020
 */
@Component
public class MQCallBackFactory {
    private static final Logger log = LoggerFactory.getLogger(MQCallBackFactory.class);

    // 异步 MQ 回调容器
    private static final ConcurrentHashMap<String, IMQBizCallback> map = new ConcurrentHashMap<>();

    @Autowired
    private IMQBizCallback[] callbacks;

    @PostConstruct
    private void init(){
        for (IMQBizCallback callback : callbacks){
            map.put(callback.getTopicTag(), callback);
        }
    }

    private static IMQBizCallback getInstance(IMQCallback callback){
        String key = callback.getTopic() + '-' + callback.getTag();
        if (!map.containsKey(key)){
            throw new ServiceException("对应异步消息不存在处理对象");
        }
        return map.get(key);
    }

    public static void onSuccess(IMQCallback callback, SendResult result) {
        getInstance(callback).onSuccess(result);
    }

    public static void onException(IMQCallback callback, Throwable e) {
        getInstance(callback).onException(e);
    }

}
