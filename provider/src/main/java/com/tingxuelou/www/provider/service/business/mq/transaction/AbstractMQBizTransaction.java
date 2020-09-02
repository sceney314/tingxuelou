package com.tingxuelou.www.provider.service.business.mq.transaction;

import com.tingxuelou.www.provider.bean.bo.MessageBo;
import com.tingxuelou.www.provider.common.constants.biz.MQDelay;
import com.tingxuelou.www.provider.common.constants.biz.TransactionStatus;
import com.tingxuelou.www.provider.utils.MQUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.common.message.Message;

/**
 * 事务消息抽象类
 * <p>
 * Date: 2020/8/28 上午12:22
 * Copyright (C), 2015-2020
 */
public abstract class AbstractMQBizTransaction implements IMQBizTransaction{
    private static final Logger log = LogManager.getLogger(AbstractMQBizTransaction.class);

    /**
     * 获取 MQ topic
     *
     * @return String
     */
    abstract String getTopic();

    /**
     * 获取 MQ tag
     *
     * @return String
     */
    abstract String getTag();

    @Override
    public String getTopicTag() {
        return getTopic() + '-' + getTag();
    }

    /**
     * 使用事务消息，本地事务具体实现
     *
     * @param bo 消息对象封装
     * @param arg 参数
     * @return TransactionStatus
     */
    abstract <T> TransactionStatus execTrans(MessageBo<T> bo, Object arg);

    /**
     * 解析消息体
     *
     * @param bytes 二进制数据
     * @return MessageBo
     */
    <T> MessageBo<T> parseMessageBody(byte[] bytes){
        return parseMessageBody(MQUtils.byte2String(bytes));
    }

    abstract <T> MessageBo<T> parseMessageBody(String con);

    /**
     * 将消息还原成 MessageBo
     *
     * @param msg 消息
     * @return MessageBo
     */
    final <T> MessageBo<T> parseMsg(Message msg){
        MessageBo<T> bo = parseMessageBody(msg.getBody());
        bo.setTopic(msg.getTopic());
        bo.setTag(msg.getTags());
        bo.setKey(msg.getKeys());
        bo.setDelay(MQDelay.getByCode(msg.getDelayTimeLevel()));
        // todo
        bo.setBizId(0L);
        return bo;
    }

    @Override
    public TransactionStatus executeLocalTransaction(Message msg, Object arg) {
        try{
            return execTrans(parseMsg(msg), arg);
        }catch (Exception e){
            log.warn("本地事务执行异常", e);
            return TransactionStatus.ROLLBACK;
        }
    }
}
