package com.kedacom.ysl.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kedacom.ysl.rabbitmq.sender.MessageBody;
import com.kedacom.ysl.rabbitmq.sender.MessageSender;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RabbitmqTest {

    @Autowired
    private MessageSender messageSender;

    @Test
    public void testSendMessage() {
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("userName", "ysl");
	data.put("password", "930925");
	MessageBody msg = new MessageBody("system", "test", "ysl_test", data);
	messageSender.sendData(msg, "ysl.test.wy");
    }
}
