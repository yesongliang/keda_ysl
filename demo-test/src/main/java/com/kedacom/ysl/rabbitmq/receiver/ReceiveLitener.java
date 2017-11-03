package com.kedacom.ysl.rabbitmq.receiver;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.kedacom.ysl.rabbitmq.sender.FastJsonMessageConverter;
import com.kedacom.ysl.rabbitmq.sender.MessageBody;

public class ReceiveLitener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger("ysl_test");

    @Resource(name = "jsonMessageConverter")
    private FastJsonMessageConverter fastJsonMessageConverter;

    @Override
    public void onMessage(Message message) {
	MessageBody msg = fastJsonMessageConverter.fromMessage(message, new MessageBody());
	logger.info(String.format("[ReceiveLitener] onMessage message=%s", msg));
    }

}
