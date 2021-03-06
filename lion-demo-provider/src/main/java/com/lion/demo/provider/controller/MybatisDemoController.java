package com.lion.demo.provider.controller;

import com.github.pagehelper.PageHelper;
import com.lion.demo.provider.mapper.UserInfoCustomMapper;
import com.lion.demo.provider.mapper.UserInfoMapper;
import com.lion.demo.provider.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * MybatisDemoController
 * TODO
 *
 * @author Yanzheng 严正
 * @date 2019/01/09
 * Copyright 2019 Yanzheng. All rights reserved.
 */
@Api("Mybatis相关示例代码类说明文档")
@RestController
public class MybatisDemoController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoCustomMapper userInfoCustomMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @ApiOperation(value = "使用Mybatis方式插入数据", notes = "包含事物")
    @ApiParam(name = "isRollback", value = "是否触发事物回滚", defaultValue = "false", required = true)
    @RequestMapping(value = "/mybatis/save", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public String mybatisSave(@RequestParam(defaultValue = "false") boolean isRollback) {

        String id = UUID.randomUUID().toString();
        if (!isRollback) {
            id = id.replaceAll("-", "");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        userInfo.setUserName("MybatisDemo");
        userInfo.setUserPassword("000000");
        userInfo.setUserBirthday(new Date());
        userInfo.setUserType(1);
        userInfo.setUserStatus(true);
        userInfo.setUserJointime(new Date());

        userInfoMapper.insertSelective(userInfo);

        return "插入成功";
    }

    @ApiOperation("Mybatis自定义API接口，注解SQL方式查询")
    @RequestMapping(value = "/mybatis/custom/list", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Object> mybatisCustomList() {
        List<Object> list = userInfoCustomMapper.selectAll();
        return list;
    }

    @ApiOperation("Mybatis自定义sqlMap查询，并分页")
    @RequestMapping(value = "/mybatis/sql/page", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Object> mybatisSqlPage() {
        PageHelper.offsetPage(0, 5);
        List<Object> list = sqlSessionTemplate.selectList("com.lion.demo.provider.mapper.UserInfoCustomMapper.getAll");
        return list;
    }

}