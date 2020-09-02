package com.tingxuelou.www.provider.mq;

import com.tingxuelou.www.provider.utils.MQUtils;
import com.tingxuelou.www.provider.utils.RedisUtils;
import com.tingxuelou.www.provider.utils.StringUtils;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 监听器抽象类
 * <p>
 * Date: 2020/8/28 下午3:08
 * Copyright (C), 2015-2020
 */
@Data
public abstract class AbstractListener {
    private static final Logger log = LogManager.getLogger(AbstractListener.class);

    // topic
    private String topic;

    // tags
    private String tags;

    // 消费者名字
    private String consumerName;

    // 是否过滤重复消息
    private boolean filterDuplicate;

    // 备注
    private String remark;

    /**
     * 具体消息消费
     *
     * @param content 消费内容
     * @return boolean
     */
    public abstract boolean readMessage(String content);

    /**
     * 获取消息 key
     *
     * @param msg 单个消息体
     * @return String
     */
    public abstract String getMessageCacheKey(MessageExt msg);

    /**
     * 单个消息处理
     *
     * @param msg 单个消息体
     * @return boolean
     */
    public boolean consumeSingleMessage(MessageExt msg){
        boolean result = false;
        long startTime = System.currentTimeMillis();
        try {
            String repeatMsgKey = getMessageCacheKey(msg);
            if (isFilterDuplicate()) {
                //需要过滤重复消息
                try {
                    String receivedTimesStr = RedisUtils.get(repeatMsgKey);
                    if (StringUtils.isNotEmpty(receivedTimesStr)) {
                        log.info("receive rocketmq has filter the repeat msg,key:{}", repeatMsgKey);
                        return true;
                    }
                } catch (Exception e) {
                    log.warn("rocketmq consumer found redis error!", e);
                }
            }

            String content = MQUtils.byte2String(msg.getBody());
            log.info("receive rocketmq,type=orderly,topic:{} key:{}, content:{}", msg.getTopic(), repeatMsgKey, content);
            result = readMessage(content);
            if (!result) {
                log.warn("receive rocketmq result status wrong,key:{}", repeatMsgKey);
            } else {
                //本条消息消费成功
                if (filterDuplicate) {
                    //如果需要过滤重复消息，并且本次已经正确消费了。标记该key，设置过期时间10分钟。（10分钟以内不会再收到重复消息）
                    RedisUtils.setWithExpire(repeatMsgKey, "1", 86400);
                }
            }
        } catch (Exception e) {
            log.warn("receive rocketmq catch an exception!", e);
            result = false;
        } finally {
            log.info("topic {} cost time {} ms", msg.getTopic(), System.currentTimeMillis() - startTime);
        }

        return result;
    }

}
