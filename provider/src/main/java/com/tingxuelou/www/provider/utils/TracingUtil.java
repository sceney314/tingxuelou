package com.tingxuelou.www.provider.utils;

import java.util.UUID;

/**
 * Date: 2020/9/2 下午7:19
 * Copyright (C), 2015-2020
 */
public class TracingUtil {

    /**
     * 产生一个32位的UUID
     *
     * @return String
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 获取当前线程的traceId
     *
     * @return
     */
//    public static String getTraceId() {
//        return AbstractInvokingTracer.getTraceId();
//    }

    /**
     * 获取下一个rpcId
     *
     * @return
     */
//    public static String nextRpcId() {
//        return AbstractInvokingTracer.nextRpcId();
//    }

    /**
     * 获取当前rpcId
     *
     * @return
     */
//    public static String getRpcId() {
//        return AbstractInvokingTracer.getRpcId();
//    }
//
//    /**
//     * 开始追踪，用于在当前线程中发送请求，该方法内部会处理tracId和rpcId
//     *
//     * @param remoteIP
//     *            对方IP
//     * @param remotePort
//     *            对方端口
//     * @param interfaceName
//     *            调用的接口
//     * @param requestType
//     *            请求类型
//     */
//    public static void beginTracing(final String remoteIP, final int remotePort, final String interfaceName,
//                                    final RequestType requestType) {
//        if (interfaceName == null)
//            throw new NullPointerException("interfaceName cannot be null");
//        beginTracing(getTraceId(), nextRpcId(), remoteIP, remotePort, interfaceName, requestType);
//    }
//
//    /**
//     * 开始追踪，用于在异步线程中发送请求
//     *
//     * @param traceId
//     *            需要在调用这个方法前调用{@link #getTraceId()}获得
//     * @param rpcId
//     *            需要在调用这个方法前调用{@link #nextRpcId()}获得
//     * @param remoteIP
//     *            对方IP
//     * @param remotePort
//     *            对方端口
//     * @param interfaceName
//     *            调用的接口
//     * @param requestType
//     *            请求类型
//     */
//    public static void beginTracing(final String traceId, final String rpcId, final String remoteIP,
//                                    final int remotePort, final String interfaceName, final RequestType requestType) {
//        if (interfaceName == null)
//            throw new NullPointerException("interfaceName cannot be null");
//        Span span = null;
//        try {
//            if (!TracerContextWathcerImpl.isTraceOff()) {
//                span = AbstractInvokingTracer.createSpanInClient(traceId, rpcId, interfaceName, remoteIP, remotePort,
//                        requestType);
//                SpanStackSupport.addAnnotation(Annotation.CLIENT_SEND);
//            }
//        } catch (Throwable e) {
//            LOG.error(
//                    String.format("<silt4j> %s catch an exception in beginTracing, ", Thread.currentThread().getName()),
//                    e);
//            if (span != null)
//                SpanStackSupport.removeSpan(span);
//        }
//    }
//
//    /**
//     * 结束追踪，该方法一定要和开始追踪的方法在同一个线程中调用
//     *
//     * @param rpcResult
//     */
//    public static void endTracing(final int rpcResult) {
//        try {
//            if (!TracerContextWathcerImpl.isTraceOff())
//                SpanStackSupport.setRpcResult(rpcResult);
//        } catch (Throwable e) {
//            LOG.error(String.format("<silt4j> %s catch an exception in endTracing", Thread.currentThread().getName()),
//                    e);
//        } finally {
//            AbstractInvokingTracer.afterInvoke(true);
//            TracingContextSupport.clearContext();
//        }
//    }
//
//    /**
//     * 用户添加自定义BinaryAnnotaiton
//     *
//     * @param type
//     *
//     * @param key
//     *
//     * @param value
//     *
//     * @return
//     */
//    public static boolean addBinaryAnnotation(final String type, final String key, final String value) {
//        return addBinaryAnnotation(new BinaryAnnotation(key, value, type));
//    }
//
//    /**
//     * 用户添加自定义BinaryAnnotaiton
//     *
//     * @param binaryAnnotation
//     *
//     * @return
//     */
//    public static boolean addBinaryAnnotation(final BinaryAnnotation binaryAnnotation) {
//        final Span span = SpanStackSupport.peekSpan();
//        if (span != null) {
//            span.addBinaryAnnotation(binaryAnnotation);
//            return true;
//        } else
//            return false;
//    }
//
//    /**
//     * 用户添加异常
//     *
//     * @param throwable
//     */
//    public static void addException(final Throwable throwable) {
//        SpanStackSupport.addException(throwable);
//    }
//
//    public static TracingContext getTraceContext() {
//        return AbstractInvokingTracer.getTracingContext();
//    }
}
