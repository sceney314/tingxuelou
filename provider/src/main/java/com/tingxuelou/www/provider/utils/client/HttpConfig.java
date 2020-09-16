package com.tingxuelou.www.provider.utils.client;

import com.tingxuelou.www.provider.common.constants.TxlConst;
import lombok.Data;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.util.List;

/**
 * http 配置
 * <p>
 * Date: 2020/9/8 下午2:43
 * Copyright (C), 2015-2020
 */
@Data
public class HttpConfig {
    // 请求次数
    private int retryTime = TxlConst.HTTP_DEFAULT_TRY_TIME;

    // 超时时间
    private int timeout = TxlConst.HTTP_DEFAULT_TIME_OUT;

    // 重定向 handler
    private LaxRedirectStrategy laxRedirectStrategy;

    // http 请求重试 handler
    private HttpRequestRetryHandler httpRequestRetryHandler;

    // http 请求拦截器，如果有依赖顺序，添加的时候必须按照顺序添加
    private List<HttpRequestInterceptor> httpRequestInterceptorList;

    // http 请求长连接保持自定义策略
    private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
}
