package com.kedacom.ysl.rabbitmq.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHandler implements MessageHandler {

    private Logger logger = LoggerFactory.getLogger("ysl_test");

    private String message;

    public void run() {
	handle();
    }

    public void handle() {
	logger.info(String.format("[TestHandler] handle, message=%s", message));
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getMessage() {
	return message;
    }
}
