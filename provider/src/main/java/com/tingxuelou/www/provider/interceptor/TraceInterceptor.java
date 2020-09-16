package com.tingxuelou.www.provider.interceptor;

import com.tingxuelou.www.provider.common.constants.TxlConst;
import com.tingxuelou.www.provider.utils.TraceHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * traceId
 * <p>
 * Date: 2020/9/2 下午7:13
 * Copyright (C), 2015-2020
 */
public class TraceInterceptor implements HandlerInterceptor {
    private static final Logger log = LogManager.getLogger(TraceInterceptor.class);

    private Map<String, String> getHttpRequestParams(HttpServletRequest request) {
        Enumeration<String> paramsName = request.getParameterNames();
        TreeMap<String, String> params = new TreeMap<>();

        while (paramsName.hasMoreElements()) {
            String name = paramsName.nextElement();
            params.put(name, request.getParameter(name));
        }

        return params;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            TraceHelper.beginTrace(request);

            Map<String, String> params = getHttpRequestParams(request);
            log.info(params);
        } catch (Exception e) {
            log.warn("preHandle", e);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            String startTime = (String)MDC.get(TxlConst.MDC_START_TIME);
            if (startTime != null) {
                log.info("costtime = {}ms", System.currentTimeMillis() - NumberUtils.toLong(startTime));
            }
        } catch (Exception e) {
            log.error("afterCompletion", e);
        } finally {
            TraceHelper.endTrace();
        }
    }
}
