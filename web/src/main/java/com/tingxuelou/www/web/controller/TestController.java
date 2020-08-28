package com.tingxuelou.www.web.controller;

import com.tingxuelou.www.api.base.BaseResult;
import com.tingxuelou.www.api.base.Result;
import com.tingxuelou.www.api.bo.TestBo;
import com.tingxuelou.www.provider.bean.model.Group;
import com.tingxuelou.www.provider.dao.GroupDao;
import com.tingxuelou.www.provider.service.dubbo.TestDubboService;
import com.tingxuelou.www.provider.utils.IdgentTestUtil;
import com.tingxuelou.www.provider.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 * <p>
 * Date: 2020-07-31 19:59
 * Copyright (C), 2015-2020
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private TestDubboService testService;


    @RequestMapping(value = "/addGroup")
    @ResponseBody
    public Result addGroup(){
        Group group = new Group();
        group.setGroupId(IdgentTestUtil.nextId() + "");
        group.setGroupName("test" + System.currentTimeMillis());
        group.setGroupDesc("test" + System.currentTimeMillis());
        group.setIsDel("2");
        group.setCreateTime(LocalDateTime.now());
        group.setUpdateTime(LocalDateTime.now());

        int row = groupDao.addGroup(group);
        return BaseResult.success(row);
    }


    @RequestMapping(value = "/testCache")
    @ResponseBody
    public Result testCache(){
        String key = "justTestInApplication";
        try {
            RedisUtils.setWithExpire(key, LocalDateTime.now().toString(), 3);
            int i = 5;
            while (i-- > 0){
                System.out.println("i=" + i + ", value=" + RedisUtils.get(key) + ", " + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return BaseResult.success();
    }

    @RequestMapping(value = "/dubbo")
    @ResponseBody
    public Result dubbo(String msg){
        TestBo bo = new TestBo();
        bo.setMsg(msg);
        return testService.test(bo);
    }
}
