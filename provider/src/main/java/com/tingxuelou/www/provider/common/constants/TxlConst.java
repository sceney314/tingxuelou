package com.tingxuelou.www.provider.common.constants;

import com.tingxuelou.www.provider.utils.PropertyUtils;

/**
 * 常量
 * <p>
 * Date: 2020-08-03 00:10
 * Copyright (C), 2015-2020
 */
public class TxlConst {
    public final static String MDC_URI = "mdc_uri";
    public final static String MDC_START_TIME = "mdc_startTime";
    public final static String MDC_IP = "mdc_ip";
    public final static String MDC_IP_PORT = "mdc_ip_port";
    public final static String MDC_MEMBER_ID = "mdc_member_id";
    public final static String MDC_TRACE_ID = "trace_id";

    public final static String ERR_SIGN = "签名 sign 校验未通过";
    public final static String ERR_PARAM = "参数非法";
    public final static String ERR_TYPE_MISMATCH = "参数类型不匹配";
    public final static String ERR_DUPLICATE_KEY = "唯一键冲突异常";
    public final static String ERR_SYSTEM = "服务繁忙，请稍后重试";

    public static final String APPLICATION = PropertyUtils.getString("application.name");
    public static final String NAME_SERVER = PropertyUtils.getString("rocketmq.nameServer");

    public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    // 成功
    public static final Integer CODE_SUCCESS = 0;
    public static final Integer CODE_FAIL = 1;

    /**
     * http 请求相关
     */
    // 默认重试次数 2
    public static final int HTTP_DEFAULT_TRY_TIME = 2;
    // 默认超时事假设置为 10 秒
    public static final int HTTP_DEFAULT_TIME_OUT = 10;
}
