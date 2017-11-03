package com.kedacom.ysl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlTask {
    private static Logger logger = LoggerFactory.getLogger("ysl_test");

    public void check() {
	logger.info(String.format("[TestXmlTask] check  start..."));
	System.out.println("7777777");
	logger.info(String.format("[TestXmlTask] check  end..."));
    }
}
