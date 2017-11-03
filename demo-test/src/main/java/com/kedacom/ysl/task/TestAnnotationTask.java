package com.kedacom.ysl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestAnnotationTask {
    private static Logger logger = LoggerFactory.getLogger("ysl_test");

    @Scheduled(cron = "0 */40 * * * ?")
    public void check() {
	logger.info(String.format("[TestAnnotationTask] check  start..."));
	System.out.println("666");
	logger.info(String.format("[TestAnnotationTask] check  end..."));
    }
}
