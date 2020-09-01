package com.tingxuelou.www.provider.init;

import org.springframework.context.ApplicationContext;

/**
 * 初始化服务约束
 * <p>
 * Date: 2020/9/1 下午7:10
 * Copyright (C), 2015-2020
 */
public interface InitAware {
    /**
     * 启动入口
     * 1. 环境信息初始化，如：连接池初始化，本地配置文件加载，本地网络连通性check等
     * 2. 业务信息初始化，如：缓存预热，LocalCache预取，业务开关预置等
     *
     * @param context ApplicationContext
     */
    void init(ApplicationContext context);

    /**
     * 销毁入口
     *
     * @param context ApplicationContext
     */
    void destroy(ApplicationContext context);
}
