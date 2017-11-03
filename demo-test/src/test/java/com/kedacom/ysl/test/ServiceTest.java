package com.kedacom.ysl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kedacom.ysl.model.User;
import com.kedacom.ysl.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void getuser() {
	User user = testService.getUser(7);
	System.out.println(user);
    }
}
