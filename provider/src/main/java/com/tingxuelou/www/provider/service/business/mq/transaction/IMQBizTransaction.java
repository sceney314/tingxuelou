package com.tingxuelou.www.provider.service.business.mq.transaction;

import com.tingxuelou.www.provider.common.constants.biz.TransactionStatus;
import org.apache.rocketmq.common.message.Message;

/**
 * MQ 业务事务
 * <p>
 * Date: 2020/8/28 上午12:20
 * Copyright (C), 2015-2020
 */
public interface IMQBizTransaction {
    /**
     * 获取 MQ topic 和 tag
     *
     * @return String
     */
    String getTopicTag();

    /**
     * 执行事务
     *
     * @param msg 消息
     * @param arg 参数
     * @return LocalTransactionState
     */
    TransactionStatus executeLocalTransaction(Message msg, Object arg);

}
