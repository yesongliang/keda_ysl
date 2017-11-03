package com.kedacom.ysl.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kedacom.ysl.dao.UserDao;
import com.kedacom.ysl.logAOP.annotation.AnnoMethod;
import com.kedacom.ysl.model.User;
import com.kedacom.ysl.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Resource
    private UserDao userDao;

    private static Logger logger = LoggerFactory.getLogger("ysl_test");

    @AnnoMethod(description = "登录", type = "test")
    public User login(String userName) {
	logger.info(String.format("[TestServiceImpl] login userName=%s", userName));
	User user = userDao.selectByName(userName);
	return user;
    }

    @Override
    public User getUser(int id) {
	User user = userDao.getUser(id);
	return user;
    }

    @Override
    public void upPhoto(byte[] photo, String userName) {
	userDao.upPhoto(photo, userName);
    }
}
