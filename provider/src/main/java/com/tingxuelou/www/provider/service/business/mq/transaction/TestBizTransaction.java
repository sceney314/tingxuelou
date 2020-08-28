package com.tingxuelou.www.provider.service.business.mq.transaction;

import com.tingxuelou.www.provider.bean.bo.MessageBo;
import com.tingxuelou.www.provider.common.constants.biz.TransactionStatus;
import org.springframework.stereotype.Component;

/**
 * 测试事务小
 * <p>
 * Date: 2020/8/28 上午10:46
 * Copyright (C), 2015-2020
 */
@Component
public class TestBizTransaction extends AbstractMQBizTransaction{
    @Override
    String getTopic() {
        return null;
    }

    @Override
    String getTag() {
        return null;
    }

    @Override
    <T> TransactionStatus execTrans(MessageBo<T> bo, Object arg) {
        return null;
    }

    @Override
    <T> MessageBo<T> parseMessageBody(String con) {
        return null;
    }
}
