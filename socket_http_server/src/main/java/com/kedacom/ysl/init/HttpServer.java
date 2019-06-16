package com.kedacom.ysl.init;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StringUtils;

import com.kedacom.ysl.dto.Request;
import com.kedacom.ysl.dto.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程池配置
 * 
 * @author ysl
 *
 */
@Slf4j
public class HttpServer {

	private String serverPort;

	private TaskExecutor taskExecutor;

	public HttpServer(String serverPort, TaskExecutor taskExecutor) {
		this.serverPort = serverPort;
		this.taskExecutor = taskExecutor;
	}

	@SuppressWarnings("resource")
	public void run() {
		ServerSocket serverSocket = null;
		int port;
		if (StringUtils.isEmpty(serverPort)) {
			port = 80;
		} else {
			port = Integer.parseInt(serverPort);
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		log.info("服务启动成功，监听端口:{}", port);
		while (true) {
			try {
				/* 实例化客户端，固定套路，通过服务端接受的对象，生成相应的客户端实例 */
				Socket socket = serverSocket.accept();
				Handler handler = new Handler(socket);
				taskExecutor.execute(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class Handler implements Runnable {

		private Socket socket;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				Request request = new Request();
				Response response = new Response(socket.getOutputStream());
				String parseMessgae = request.parse(socket.getInputStream());
				if (parseMessgae != null) {
					log.warn("请求解析出现错误,message={}", parseMessgae);
					response.writeHtml(400, "Bad Request", "The server cannot or will not process the request due to .....\\n" + parseMessgae);
				}
				WebContainer.getWebContainer().doDispatch(request, response);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
