package com.kedacom.ysl.rabbitmq.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedacom.ysl.rabbitmq.config.RabbitMQConfig;

@Service("messageSender")
public class MessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    /**
     * 发送消息
     * 
     * @param message
     *            消息
     * @param routingKey
     *            路由
     */
    public void sendData(Object message, String routingKey) {
	amqpTemplate.convertAndSend(rabbitMQConfig.getExchangeName(), routingKey, message);
    }
}