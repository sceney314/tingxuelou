package com.tingxuelou.www.provider.filter;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.tingxuelou.www.api.base.BaseResult;
import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * 日志 filter
 * <p>
 * Date: 2020-08-03 00:19
 * Copyright (C), 2015-2020
 */
public class TraceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        String service = invoker.getInterface().getName() + "." + invocation.getMethodName();
        String ip = RpcContext.getContext().getRemoteAddressString();

        long startTime = System.currentTimeMillis();
        log.info("消费方{},调用接口{},请求报文{}", ip, service, JSON.toJSONString(invocation.getArguments()));
        try {
            result = invoker.invoke(invocation);
            log.info("消费方{},调用接口{},返回报文{},耗时{}毫秒",ip, service, JSON.toJSONString(result.getValue()), (System.currentTimeMillis() - startTime));
            // 错误捕获
            if (result.hasException()) {
                Throwable e = result.getException();
                log.warn("dubbo trace filter exception", e);
                if (e instanceof ServiceException) {
                    // 处理业务异常
                    ServiceException se = (ServiceException) e;
                    result = new RpcResult(BaseResult.fail(se.getErrorCode(), se.getMessage()));
                } else if (e instanceof IllegalArgumentException) {
                    result = new RpcResult(BaseResult.fail(e.getMessage()));
                } else if (e instanceof MissingServletRequestParameterException) {
                    // 方法参数类型异常
                    result = new RpcResult(BaseResult.fail(TxlConst.ERR_TYPE_MISMATCH));
                } else {
                    // 处理其他异常
                    result = new RpcResult(BaseResult.fail(TxlConst.ERR_SYSTEM));
                }
            }
        }catch (Exception e) {
            log.warn("service:{}, Exception:{}", service, e);
            result = new RpcResult(BaseResult.fail(TxlConst.ERR_SYSTEM));
        }

        return result;
    }
}
