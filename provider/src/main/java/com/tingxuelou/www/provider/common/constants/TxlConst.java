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

    public final static String ERR_SIGN = "签名 sign 校验未通过";
    public final static String ERR_PARAM = "参数非法";
    public final static String ERR_TYPE_MISMATCH = "参数类型不匹配";
    public final static String ERR_DUPLICATE_KEY = "唯一键冲突异常";
    public final static String ERR_SYSTEM = "服务繁忙，请稍后重试";

    public static final String APPLICATION = PropertyUtils.getString("application.name");
}
