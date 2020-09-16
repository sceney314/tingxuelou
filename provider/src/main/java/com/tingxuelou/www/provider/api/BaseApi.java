package com.tingxuelou.www.provider.api;

import com.tingxuelou.www.api.base.Result;
import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.exceptions.ServiceException;
import com.tingxuelou.www.provider.utils.StringUtils;
import com.tingxuelou.www.provider.utils.TracingUtil;
import com.tingxuelou.www.provider.utils.client.Clients;
import com.tingxuelou.www.provider.utils.client.HttpConfig;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * api基础信息
 * <p>
 * Date: 2020/9/8 下午3:15
 * Copyright (C), 2015-2020
 */
public abstract class BaseApi {
    private static final Logger log = LogManager.getLogger(BaseApi.class);
    /**
     * 所有的子类都必须想 Clients 注册自己特性，包括不限于 http 请求拦截器、配置、重定向等信息。
     */
    void register(){
        Clients.register(buildHttpConfig(), this.getClass());
    }

    /**
     * 生成 HttpConfig
     * 所有的子类丰富 HttpConfig，包括不限于 http 请求拦截器、配置、重定向等信息。
     *
     * @return HttpConfig
     */
    HttpConfig buildHttpConfig(){
        final HttpConfig config = new HttpConfig();
        config.setHttpRequestRetryHandler((exception, executionCount, context) -> {
            if (executionCount >= config.getRetryTime()) {
                // Do not retry if over max retry count
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }

            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }

            // 超时
            if (exception instanceof InterruptedIOException) {
                return true;
            }

            // 目标服务器不可达
            if (exception instanceof UnknownHostException) {
                return false;
            }

            // ssl握手异常
            if (exception instanceof SSLException) {
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return  !(request instanceof HttpEntityEnclosingRequest);
        });
        return config;
    }

    /**
     * 判断是否请求成功
     *
     * @param result 结果对象
     * @return boolean
     */
    public static boolean isSuccess(Result<?> result){
        return !isFail(result);
    }

    /**
     * 判断是否请求失败
     *
     * @param result 结果对象
     * @return boolean
     */
    public static boolean isFail(Result<?> result){
        if (result == null || result.getError() == null || !result.getError().getReturnCode().equals(TxlConst.CODE_SUCCESS)){
            return true;
        }

        return false;
    }

    public String doPost(String url, Map<String, String> paramMap){
        return doPost(url, paramMap, null, "utf8");
    }

    public String doPost(String url, Map<String, String> paramMap, Map<String, String> headers){
        return doPost(url, paramMap, headers, "utf8");
    }

    public String doPost(String url, Map<String, String> paramMap, Map<String, String> headers, String encode){
        HttpEntity entity = post(url, paramMap, headers, encode);
        try {
            return EntityUtils.toString(entity);
        }catch (IOException | ParseException e){
            log.warn("HttpEntity parse to string exception", e);
            throw new ServiceException("返回结果解析异常");
        }
    }

    /**
     * post 请求
     *
     * @param url 请求地址
     * @param paramMap 参数 map
     * @param encode 编码
     * @param headers 请求头
     * @return HttpEntity
     */
    public HttpEntity post(String url, Map<String, String> paramMap, Map<String, String> headers, String encode){
        long start = System.currentTimeMillis();
        CloseableHttpClient client = Clients.getClient(this.getClass());
        HttpPost httpPost = new HttpPost(url);
        // 设置全链路
        setTraceInfo(headers);
        // 设置自定义头
        setPostHead(httpPost, headers);
        List<BasicNameValuePair> postData = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()){
            postData.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, Charset.forName(encode));
        httpPost.setEntity(entity);
        HttpEntity httpEntity = null;
        log.info("url:{}, params:{}", url, postData.toString());
        try {
            CloseableHttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            log.info("url:{}, params:{}, result:{}", url, postData.toString(), EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            log.warn("url:{}, 请求第三方异常:{}", url, e);
            throw new ServiceException("请求第三方异常");
        }finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                log.info("net io exception", e);
            }
            log.info("url:{}, cost:{} params:{}", url, System.currentTimeMillis() - start, postData.toString());
        }

        return httpEntity;
    }

    /**
     * 设置全链路追踪信息
     *
     * @param headers 头信息
     */
    private void setTraceInfo(Map<String, String> headers) {
        String traceId = MDC.get(TxlConst.MDC_TRACE_ID);
        if (StringUtils.isEmpty(traceId)){
            MDC.put(TxlConst.MDC_TRACE_ID, TracingUtil.generateUUID());
            traceId = MDC.get(TxlConst.MDC_TRACE_ID);
        }
        if (CollectionUtils.isEmpty(headers)){
            headers.put(TxlConst.MDC_TRACE_ID, traceId);
        }
    }

    /**
     * 设置 post 头信息
     *
     * @param post httpPost 对象
     * @param headers 自定义头部信息
     */
    private void setPostHead(HttpPost post, Map<String, String> headers){
        for (Map.Entry<String, String> entry : headers.entrySet()){
            BasicHeader header = new BasicHeader(entry.getKey(), entry.getValue());
            post.addHeader(header);
        }
    }
}
