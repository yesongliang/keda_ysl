package com.kedacom.ysl.rabbitmq.receiver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kedacom.ysl.rabbitmq.MessageType;
import com.kedacom.ysl.rabbitmq.handler.MessageHandler;
import com.kedacom.ysl.rabbitmq.handler.TestHandler;

public class MessageReveiverFactory {

    private Logger logger = LoggerFactory.getLogger("ysl_test");

    private Map<String, Class<? extends MessageHandler>> handlers = new HashMap<String, Class<? extends MessageHandler>>();

    /**
     * 初始化消息处理器和消息接收线程
     */
    public void init() {
	initHandlers();
	startReceiveThread();
    }

    /**
     * 开启RabbitMQ消息接收线程
     */
    private void startReceiveThread() {
	logger.info("[MessageReveiverFactory] 开启消息接收线程！");
	MessageReceiver msgReceiver = new MessageReceiver("rabbitmq_receiver", this);
	Thread t = new Thread(msgReceiver, "rabbitmq_receiver_thread");
	t.setDaemon(true);
	t.start();
    }

    /**
     * 初始化消息处理器
     */
    private void initHandlers() {
	handlers.put(MessageType.YSL_TEST, TestHandler.class);
    }

    /**
     * 根据消息类型获取消息处理器
     * 
     * @param messageType
     * @return
     */
    public MessageHandler getHandler(String messageType) {
	logger.info("[MessageReveiverFactory] getHandler messageType=" + messageType);
	try {
	    Class<? extends MessageHandler> clazz = this.handlers.get(messageType);
	    if (clazz == null) {
		logger.info("[MessageReveiverFactory] getHandler clazz is NULL");
		return null;
	    }
	    return clazz.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {
	    logger.info("[MessageReveiverFactory] getHandler 出现异常=%s" + e.getMessage());
	    e.printStackTrace();
	}
	return null;
    }
}
