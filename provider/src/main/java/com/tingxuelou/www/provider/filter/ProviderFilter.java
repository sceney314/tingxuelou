package com.tingxuelou.www.provider.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.tingxuelou.www.api.base.BaseResult;
import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者 filter
 * <p>
 * Date: 2020-08-03 00:06
 * Copyright (C), 2015-2020
 */
@Activate(group = {"provider"})
public class ProviderFilter implements Filter {
    private static final Logger log = LogManager.getLogger(ProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 获取consumerIP
        long startTime = System.currentTimeMillis();
        MDC.put(TxlConst.MDC_IP, RpcContext.getContext().getRemoteAddress().getHostString());
        MDC.put(TxlConst.MDC_IP_PORT, String.valueOf(RpcContext.getContext().getRemoteAddress().getPort()));
        String consumerIp = RpcContext.getContext().getRemoteAddressString();
        String serviceName = invoker.getInterface().getName();
        String service = serviceName + "." + invocation.getMethodName();
        MDC.put(TxlConst.MDC_URI, service);
        Map<String, Object> allParam = new HashMap<>();
        allParam.put("public", invocation.getAttachments());
        allParam.put("business", invocation.getArguments());

        // 输出请求日志
        log.info("消费方:{} 调用接口:{} 请求报文:{}", consumerIp, service, JSON.toJSONString(allParam));

        Result result = null;
        try{
            // 服务调用
            result = invoker.invoke(invocation);

            // 错误捕获 标准返回Result
            if (result.hasException()) {
                Throwable e = result.getException();
                log.warn("ProviderFilter exception", e);
                if (e instanceof ServiceException) {
                    // 处理业务异常
                    throw (ServiceException) e;
                } else if (e instanceof IllegalArgumentException) {
                    throw new ServiceException(e.getMessage());
                } else if (e instanceof MissingServletRequestParameterException) {
                    // 方法参数类型异常
                    throw new ServiceException(TxlConst.ERR_TYPE_MISMATCH);
                } else {
                    // 处理其他异常
                    throw new ServiceException(TxlConst.ERR_SYSTEM);
                }
            }
        }catch (ServiceException se){
            log.warn("Service ServiceException", se);
            result = new RpcResult(BaseResult.fail(se.getErrorCode(), se.getMessage()));
        }catch (IllegalArgumentException ie){
            log.warn("Service Exception", ie);
            result = new RpcResult(BaseResult.fail(ie.getMessage()));
        } catch(Exception e){
            log.warn("Service Exception", e);
            result = new RpcResult(BaseResult.fail(TxlConst.ERR_SYSTEM));
        }

        // 输出返回日志
        log.info("消费方:{} 调用接口:{} 返回报文:{} 耗时:{}毫秒", consumerIp, service, JSON.toJSONString(result.getValue()), (System.currentTimeMillis() - startTime));
        return result;
    }
}
