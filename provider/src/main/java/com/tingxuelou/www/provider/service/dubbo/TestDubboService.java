package com.tingxuelou.www.provider.service.dubbo;

import com.tingxuelou.www.api.base.BaseResult;
import com.tingxuelou.www.api.base.Result;
import com.tingxuelou.www.api.bo.TestBo;
import com.tingxuelou.www.api.service.ITestService;
import com.tingxuelou.www.api.vo.TestVo;
import org.springframework.stereotype.Service;

/**
 * Date: 2020-08-03 09:51
 * Copyright (C), 2015-2020
 */
@Service
public class TestDubboService implements ITestService {

    @Override
    public Result<TestVo> test(TestBo bo) {
        return BaseResult.success(new TestVo(bo.getMsg()));
    }
}
