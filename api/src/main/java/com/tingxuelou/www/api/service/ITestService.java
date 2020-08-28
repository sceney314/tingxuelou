package com.tingxuelou.www.api.service;

import com.tingxuelou.www.api.base.Result;
import com.tingxuelou.www.api.bo.TestBo;
import com.tingxuelou.www.api.vo.TestVo;

/**
 * 测试 dubbo
 * <p>
 * Date: 2020-08-03 09:48
 * Copyright (C), 2015-2020
 */
public interface ITestService {

    Result<TestVo> test(TestBo bo);
}
