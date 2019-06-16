package com.kedacom.ysl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;

import com.kedacom.ysl.init.HttpServer;
import com.kedacom.ysl.init.WebContainer;

/**
 * 基于socketServer+springBoot实现简单的web server
 * 
 * 
 * @author 95488
 * @createDate 2019年6月13日
 *
 */
@SpringBootApplication
public class App implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	private Environment environment;

	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// 维护容器
		WebContainer.getWebContainer().init("com.kedacom.ysl.controller");

		// 启动socketServer服务接收http请求
		String serverPort = environment.getProperty("server.port");
		new HttpServer(serverPort, taskExecutor).run();
	}

}
