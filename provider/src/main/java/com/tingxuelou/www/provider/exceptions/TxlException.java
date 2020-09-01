package com.tingxuelou.www.provider.exceptions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.tingxuelou.www.provider.common.constants.TxlConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常兜底
 * <p>
 * Date: 2020/8/28 下午8:04
 * Copyright (C), 2015-2020
 */
@Component
public class TxlException implements HandlerExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(TxlException.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        log.warn("Error", ex);
        log.warn("HttpServletRequest:{}", request);
        log.warn("HttpServletResponse:{}", response);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("exception");

        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("traceId", MDC.get(Constants.MDC_TRACE_ID));
        attributes.put("data", "");

        Map<String, Object > errorMap = new HashMap<>();
        FastJsonJsonView view = new FastJsonJsonView();
        int code = TxlConst.CODE_FAIL;
        if(ex instanceof ServiceException){
            // 业务异常
            code = ((ServiceException) ex).getErrorCode();
            errorMap.put("returnMessage", ex.getMessage());
        }else if(ex instanceof IllegalArgumentException){
            // 参数异常
            errorMap.put("returnMessage", ex.getMessage());
        }else if(ex instanceof MissingServletRequestParameterException){
            // 方法缺少参数
            errorMap.put("returnMessage", "方法缺少参数");
        }else if(ex instanceof org.springframework.beans.TypeMismatchException){
            // 参数类型不匹配异常
            errorMap.put("returnMessage", "参数类型不匹配异常");
        }else if(ex instanceof org.springframework.validation.BindException){
            // 方法参数绑定异常
            errorMap.put("returnMessage", "方法参数绑定异常");
        }else if(ex instanceof DuplicateKeyException){
            // 主键冲突异常
            errorMap.put("returnMessage", "主键冲突异常");
        }else{
            // 其他异常
            errorMap.put("returnMessage", "服务异常，请稍后重试");
        }

        errorMap.put("returnCode", code);
        errorMap.put("returnUserMessage", errorMap.get("returnMessage"));
        attributes.put("error", errorMap);

        log.info("txl-result:{}", JSON.toJSONString(attributes));
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
}
