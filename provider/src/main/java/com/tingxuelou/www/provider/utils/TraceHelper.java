package com.tingxuelou.www.provider.utils;

import com.tingxuelou.www.provider.common.constants.TxlConst;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * traceId 帮助类
 * <p>
 * Date: 2020/9/2 下午7:15
 * Copyright (C), 2015-2020
 */
public class TraceHelper {
    private static final Logger log = LogManager.getLogger(TraceHelper.class);

    /**
     * 开始追踪。
     * <p>用于生成追踪信息，供拦截器、执行任务的线程调用。</p>
     *
     * @param request Servlet 请求信息
     */
    public static void beginTrace(HttpServletRequest request) {
        long curTime = System.currentTimeMillis();
        String uri = "";
        String ip = "";

        if (request != null) {
            uri = request.getRequestURI();
//            ip = HttpClientUtil.getRemoteHost(request);
        }

        MDC.put(TxlConst.MDC_URI, uri);
        MDC.put(TxlConst.MDC_START_TIME, String.valueOf(curTime));
        MDC.put(TxlConst.MDC_IP, ip);

//        traceMemberID(request);
    }

    /**
     * 开始追踪。
     * <p>用于执行任务的线程，在执行任务逻辑之前，首先调用它来进行初始化（主要是 traceId、开始时间）。</p>
     *
     */
    public static void beginTrace() {
        beginTrace(null);
    }

    /**
     * 结束追踪。
     * <p>用于执行任务的线程，在任务逻辑执行完毕，交还给线程池之前，调用它来清理日志数据。</p>
     *
     */
    public static void endTrace() {
        MDC.remove(TxlConst.MDC_IP);
        MDC.remove(TxlConst.MDC_START_TIME);
        MDC.remove(TxlConst.MDC_URI);
        MDC.remove(TxlConst.MDC_MEMBER_ID);
    }

//    private static void setTracingInfoToHttpHeader(HttpUriRequest httpUriRequest) {
//        String host = "";
//        int port = -1;
//        String path = "";
//
//        URI uri = httpUriRequest.getURI();
//        if (uri != null) {
//            host = uri.getHost();
//            port = uri.getPort();
//            path = uri.getPath();
//        }
//
//        TracingUtil.beginTracing(host, port, path, RequestType.HTTP);
//
//        httpUriRequest.setHeader(TracingConstants.TRACEID, TracingUtil.getTraceId());
//        httpUriRequest.setHeader(TracingConstants.RPCID, TracingUtil.getRpcId());
//    }

//    @SuppressWarnings("unchecked")
//    public static <T extends HttpResponse> T httpRequest(HttpClient httpClient, HttpUriRequest httpUriRequest) throws IOException {
//        int rpcResult = RpcResult.SUCCESS.key;
//        T httpResponse;
//
//        try {
//            setTracingInfoToHttpHeader(httpUriRequest);
//            httpResponse = (T) httpClient.execute(httpUriRequest);
//        } catch (Exception e) {
//            rpcResult = RpcResult.FAILED.key;
//            TracingUtil.addException(e);
//
//            throw e;
//        } finally {
//            TracingUtil.endTracing(rpcResult);
//        }
//
//        return httpResponse;
//    }

    /**
     * 发起带有追踪信息的 HTTP GET 请求。
     * <p>向 httpclient 对象中自动放入 silt4j 追踪信息，并发起调用。</p>
     *
     * @param httpClient httpClient 对象
     * @param httpGet httpGet 对象
     * @return 应答
     * @throws IOException IO 异常
     */
//    public static HttpResponse httpGet(HttpClient httpClient, HttpGet httpGet) throws IOException {
//        return httpRequest(httpClient, httpGet);
//    }

    /**
     * 发起带有追踪信息的 HTTP GET 请求。
     * <p>向 httpclient 对象中自动放入 silt4j 追踪信息，并发起调用。</p>
     *
     * @param httpClient httpClient 对象
     * @param httpGet httpGet 对象
     * @return 应答
     * @throws IOException IO 异常
     */
//    public static CloseableHttpResponse httpGet(CloseableHttpClient httpClient, HttpGet httpGet) throws IOException {
//        return httpRequest(httpClient, httpGet);
//    }

    /**
     * 发起带有追踪信息的 HTTP POST 请求。
     * <p>向 httpclient 对象中自动放入 silt4j 追踪信息，并发起调用。</p>
     *
     * @param httpClient httpClient 对象
     * @param httpPost httpPost 对象
     * @return 应答
     * @throws IOException IO 异常
     */
//    public static HttpResponse httpPost(HttpClient httpClient, HttpPost httpPost) throws IOException {
//        return httpRequest(httpClient, httpPost);
//    }

    /**
     * 发起带有追踪信息的 HTTP POST 请求。
     * <p>向 httpclient 对象中自动放入 silt4j 追踪信息，并发起调用。</p>
     *
     * @param httpClient httpClient 对象
     * @param httpPost httpPost 对象
     * @return 应答
     * @throws IOException IO 异常
     */
//    public static CloseableHttpResponse httpPost(CloseableHttpClient httpClient, HttpGet httpPost) throws IOException {
//        return httpRequest(httpClient, httpPost);
//    }

    /**
     * 发起带有追踪信息的 HTTP PUT 请求。
     * <p>向 httpclient 对象中自动放入 silt4j 追踪信息，并发起调用。</p>
     * @param httpClient httpClient 对象
     * @param httpPut httpPost 对象
     * @return BasicHttpResponse
     * @throws IOException IO 异常
     */
//    public static HttpResponse httpPut(HttpClient httpClient, HttpPut httpPut) throws IOException{
//        return httpRequest(httpClient, httpPut);
//    }

    /**
     * 启动 JOB 日志追踪
     *
     * @param jobName job 名
     * @return 分配的 traceId
     */
//    public static String beginJobTracing(String jobName) {
//        TracingUtil.beginTracing("localhost", 8080, jobName, RequestType.HTTP);
//        return TracingUtil.getTraceId();
//    }

    /**
     * 终止 JOB 日志追踪
     *
     * @param rpcResult RPC 结果
     */
//    public static void endJobTracing(int rpcResult) {
//        TracingUtil.endTracing(rpcResult);
//        endTrace();
//    }

    /**
     * 增加业务注解
     *
     * @param binaryAnnotationList 业务注解列表
     * @return 成功返回 true
     */
//    public static <T> boolean addBinaryAnnotation(List<BinaryAnnotation<T>> binaryAnnotationList) {
//        if (binaryAnnotationList == null || binaryAnnotationList.size() == 0) {
//            return false;
//        }
//
//        Span span = SpanStackSupport.peekSpan();
//        if (span == null) {
//            logger.error("addBinaryAnnotation failed: span is null");
//            return false;
//        }
//
//        for (BinaryAnnotation binaryAnnotation : binaryAnnotationList) {
//            com.jiedaibao.silt4j.common.BinaryAnnotation targetBinaryAnnotation = new com.jiedaibao.silt4j.common.BinaryAnnotation();
//
//            targetBinaryAnnotation.setKey(binaryAnnotation.getTargetKey());
//            targetBinaryAnnotation.setValue(binaryAnnotation.getTargetValue());
//            targetBinaryAnnotation.setType(binaryAnnotation.getType());
//            span.addBinaryAnnotation(targetBinaryAnnotation);
//        }
//
//        return true;
//    }

    /**
     * 增加业务注解
     *
     * @param binaryAnnotationList 业务注解列表
     * @return 成功返回 true
     */
//    public static <T> boolean addBinaryAnnotation(BinaryAnnotationList<T> binaryAnnotationList) {
//        if (binaryAnnotationList == null) {
//            return false;
//        }
//
//        return addBinaryAnnotation(binaryAnnotationList.subList(0, binaryAnnotationList.size()));
//    }

//    /**
//     * 增加业务注解
//     *
//     * @param binaryAnnotation 业务注解
//     * @return 成功返回 true
//     */
//    public static <T> boolean addBinaryAnnotation(BinaryAnnotation<T> binaryAnnotation) {
//        return addBinaryAnnotation(Collections.singletonList(binaryAnnotation));
//    }
//
//    /**
//     * 增加业务注解
//     *
//     * @param key 键名
//     * @param value 键值
//     * @param type 类型
//     * @return 成功返回 true
//     */
//    public static <T> boolean addBinaryAnnotation(String key, T value, String type) {
//        BinaryAnnotation<T> binaryAnnotation = new BinaryAnnotation<>(key, value, type);
//        return addBinaryAnnotation(Collections.singletonList(binaryAnnotation));
//    }
//
//    /**
//     * 增加业务注解
//     *
//     * @param key 键名
//     * @param value 键值
//     * @return 成功返回 true
//     */
//    public static <T> boolean addBinaryAnnotation(String key, T value) {
//        return addBinaryAnnotation(key, value, null);
//    }
//
//    /**
//     * 对 memberID 进行追踪，如果存在此参数，则记录到全链路日志中
//     *
//     * @param request HTTP 请求
//     * @return 成功返回 true
//     */
//    public static boolean traceMemberID(HttpServletRequest request) {
//        if (request == null) {
//            return false;
//        }
//
//        String memberID = request.getParameter("memberID");
//
//        return traceMemberID(memberID);
//    }
//
//    /**
//     * 对 memberID 进行追踪，如果存在此参数，则记录到全链路日志中
//     *
//     * @param memberID memberID 的值
//     * @return 成功返回 true
//     */
//    public static boolean traceMemberID(String memberID) {
//        if (StringUtils.isEmpty(memberID)) {
//            return false;
//        }
//
//        boolean result = addBinaryAnnotation("memberID", memberID);
//        if (result) {
//            MDC.put(Constants.MDC_MEMBER_ID, memberID);
//        }
//
//        return result;
//    }

//    /**
//     * 业务注解
//     */
//    public static class BinaryAnnotation<T> {
//
//        private static final String[] RESERVED_KEYWORDS = { "memberID" };
//
//        private String key;
//        private T value;
//        private String type;
//
//        public BinaryAnnotation() {
//        }
//
//        public BinaryAnnotation(String key, T value, String type) {
//            this.key = key;
//            this.value = value;
//            this.type = type;
//        }
//
//        public BinaryAnnotation(String key, T value) {
//            this(key, value, null);
//        }
//
//        public String getKey() {
//            return key;
//        }
//
//        public void setKey(String key) {
//            this.key = key;
//        }
//
//        public T getValue() {
//            return value;
//        }
//
//        public void setValue(T value) {
//            this.value = value;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        /**
//         * 获得 silt4j 中可用的 key
//         *
//         * @return key
//         */
//        String getTargetKey() {
//            String targetKey = (key != null) ? key : "";
//
//            if (isReservedKeyword(targetKey)) {
//                return targetKey;
//            }
//
//            if (value instanceof Double || value instanceof Float) {
//                if (! targetKey.endsWith("_f")) {
//                    targetKey += "_f";
//                }
//            } else if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
//                if (! targetKey.endsWith("_i")) {
//                    targetKey += "_i";
//                }
//            } else {
//                if (! targetKey.endsWith("_s")) {
//                    targetKey += "_s";
//                }
//            }
//
//            return targetKey;
//        }
//
//        /**
//         * 获得 silt4j 中可用的 value
//         *
//         * @return value
//         */
//        String getTargetValue() {
//            if (value instanceof String) {
//                return (String) value;
//            } else {
//                return value == null ? "" : value.toString();
//            }
//        }
//
//        /**
//         * 是否为保留字
//         *
//         * @param key 键名
//         * @return 如果是保留字，返回 true
//         */
//        private boolean isReservedKeyword(String key) {
//            for (String reservedKeyword : RESERVED_KEYWORDS) {
//                if (reservedKeyword.equalsIgnoreCase(key)) {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }
//
//    /**
//     * 业务注解列表
//     */
//    public static class BinaryAnnotationList<T> extends ArrayList<BinaryAnnotation<T>> {
//
//        /**
//         * 增加业务注解
//         *
//         * @param key 键名
//         * @param value 键值
//         * @return 成功返回 true
//         */
//        public boolean add(String key, T value) {
//            return this.add(key, value, null);
//        }
//
//        /**
//         * 增加业务注解
//         *
//         * @param key 键名
//         * @param value 键值
//         * @param type 类型
//         * @return 成功返回 true
//         */
//        public boolean add(String key, T value, String type) {
//            BinaryAnnotation<T> binaryAnnotation = new BinaryAnnotation<>(key, value, type);
//            return this.add(binaryAnnotation);
//        }
//
//    }

}
