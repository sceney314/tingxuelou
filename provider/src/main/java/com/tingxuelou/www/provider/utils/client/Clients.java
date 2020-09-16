package com.tingxuelou.www.provider.utils.client;

import com.tingxuelou.www.provider.init.AbstractInit;
import com.tingxuelou.www.provider.utils.Assert;
import com.tingxuelou.www.provider.utils.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * http client
 * <p>
 * Date: 2020/9/7 下午4:45
 * Copyright (C), 2015-2020
 */
public class Clients extends AbstractInit {
    private static final Logger log = LogManager.getLogger(Clients.class);

    // 池连接管理器
    private static final PoolingHttpClientConnectionManager cm;

    // 池连接管理器最多创建多少个 client
    private static final Integer MAX_CLIENT = 2000;

    // 默认每个路由，有多少连接
    private static final Integer DEFAULT_MAX_PER_ROUTER = 20;

    // 监控对象
    private static final ConnectionMonitorThread monitorThread = new ConnectionMonitorThread();

    // 路由容器
    private static final ConcurrentHashMap<String, HttpRoute> routerMap = new ConcurrentHashMap<>();

    // 业务配置容器
    private static final ConcurrentHashMap<String, RequestConfig> requestConfigMap = new ConcurrentHashMap<>();

    // 业务重试句柄
    private static final ConcurrentHashMap<String, HttpRequestRetryHandler> retryHandlerMap = new ConcurrentHashMap<>();

    // 保持连接容器
    private static final ConcurrentHashMap<String, ConnectionKeepAliveStrategy> keepAliveStrategyMap = new ConcurrentHashMap<>();

    // http 请求拦截器容器
    private static final ConcurrentHashMap<String, List<HttpRequestInterceptor>> interceptorMap = new ConcurrentHashMap<>();

    // 重定向句柄容器
    private static final ConcurrentHashMap<String, LaxRedirectStrategy> redirectMap = new ConcurrentHashMap<>();

    // 配置容器
    private static final ConcurrentHashMap<String, HttpConfig> configMap = new ConcurrentHashMap<>();

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_CLIENT);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTER);
    }

    private Clients(){}

    public static CloseableHttpClient getClient(Class<?> clazz){
        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(retryHandlerMap.getOrDefault(clazz.getSimpleName(), null))
                .setKeepAliveStrategy(keepAliveStrategyMap.getOrDefault(clazz.getSimpleName(), null))
                .setDefaultRequestConfig(requestConfigMap.getOrDefault(clazz.getSimpleName(), null))
                .setRedirectStrategy(redirectMap.getOrDefault(clazz.getSimpleName(), null));

        // 拦截器是责任链模式
        if (interceptorMap.containsKey(clazz.getSimpleName())){
            for (HttpRequestInterceptor interceptor : interceptorMap.get(clazz.getSimpleName())){
                clientBuilder.addInterceptorLast(interceptor);
            }
        }

        return clientBuilder.build();
    }

    /**
     * 业务 Api 加载之后注册自己相关配置
     *
     * @param config HttpConfig 配置对象
     * @param clazz 具体类
     */
    public static void register(HttpConfig config, Class<?> clazz){
        // 注册默认 client 配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(config.getTimeout())
                .setConnectTimeout(config.getTimeout())
                .setSocketTimeout(config.getTimeout())
                .build();
        requestConfigMap.put(clazz.getSimpleName(), requestConfig);


        // 注册重试句柄，这个要求必须
        Assert.notNull(config.getHttpRequestRetryHandler(), "重试句柄不能为空");
        configMap.put(clazz.getSimpleName(), config);
        retryHandlerMap.put(clazz.getSimpleName(), config.getHttpRequestRetryHandler());

        // 注册自定义 keepAlive
        if (config.getConnectionKeepAliveStrategy() != null){
            keepAliveStrategyMap.put(clazz.getSimpleName(), config.getConnectionKeepAliveStrategy());
        }

        // 注册自定义拦截器
        if (!CollectionUtils.isEmpty(config.getHttpRequestInterceptorList())){
            interceptorMap.put(clazz.getSimpleName(), Collections.unmodifiableList(config.getHttpRequestInterceptorList()));
        }

        // 注册自定义重定向
        if (config.getLaxRedirectStrategy() != null){
            redirectMap.put(clazz.getSimpleName(), config.getLaxRedirectStrategy());
        }
    }

    public static void setMaxPerRouter(String host, int port){
        setMaxPerRouter(host, port, DEFAULT_MAX_PER_ROUTER);
    }

    /**
     * 修改路由最大 client 数量
     *
     * @param host host 地址
     * @param port 端口
     * @param maxPerRouter 最大数量
     */
    public static void setMaxPerRouter(String host, int port, int maxPerRouter){
        String addr = host + ":" + port;
        HttpRoute route = routerMap.getOrDefault(addr, null);
        if (route == null){
            routerMap.putIfAbsent(addr, new HttpRoute(new HttpHost(host, port)));
            route = routerMap.get(addr);
        }
        cm.setMaxPerRoute(route, maxPerRouter);
    }

    public static void setMultiMaxPerRouter(String addr){
        setMultiMaxPerRouter(addr, DEFAULT_MAX_PER_ROUTER);
    }

    /**
     * 支持批量地址
     *
     * @param addr 批量地址 ip:port;ip:port
     * @param maxPerRouter 最大数量
     */
    public static void setMultiMaxPerRouter(String addr, int maxPerRouter){
        String[] addresses = addr.split(";");
        for (String address : addresses){
            if (StringUtils.isEmpty(address)){
                continue;
            }

            // 解析地址，过滤非法地址，默认 80
            String[] ipPort = address.split(":");
            if (ipPort.length > 2 || ipPort.length < 1){
                continue;
            }
            setMaxPerRouter(ipPort[0], ipPort.length == 1 ? 80 : Integer.parseInt(ipPort[1]), maxPerRouter);
        }
    }

    @Override
    public void init(ApplicationContext context) {
        monitorThread.start();
    }

    @Override
    public void destroy(ApplicationContext context) {
        shutdown();
        cm.close();
    }

    /**
     * 关闭线程池
     */
    public static void shutdown(){
        monitorThread.shutdown();
    }

    /**
     * 监控线程，用于关闭过期连接 和超过 60 秒的连接
     */
    private static class ConnectionMonitorThread extends Thread{
        private volatile boolean shutdown = false;

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000L);
                        // Close expired connections
                        cm.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 60 sec
                        cm.closeIdleConnections(60, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                log.warn("处理异常 client", ex);
            }
        }

        // 线程池关闭
        private void shutdown() {
            shutdown = true;

            synchronized (this) {
                notifyAll();
            }
        }
    }
}
