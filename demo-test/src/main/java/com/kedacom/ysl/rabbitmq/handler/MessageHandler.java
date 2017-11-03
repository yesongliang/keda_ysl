package com.kedacom.ysl.rabbitmq.handler;

/**
 * RabbitMQ消息处理接口类
 * 
 * @author ysl
 * 
 */
public interface MessageHandler extends Runnable {

    public void handle();

    public void setMessage(String message);

}
