package com.kedacom.ysl.rabbitmq.receiver;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kedacom.ysl.rabbitmq.config.RabbitMQConfig;
import com.kedacom.ysl.rabbitmq.handler.MessageHandler;
import com.kedacom.ysl.util.SpringBeanUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class MessageReceiver implements Runnable {

    private Logger logger = LoggerFactory.getLogger("ysl_test");

    private MessageReveiverFactory messageReceiverFactory;

    private ExecutorService executorService;

    private String name;

    public MessageReceiver() {
    }

    /**
     * 构造函数
     * 
     * @param name
     *            消息接收器名称
     * @param messageReceiverFactory
     *            启动消息接收器的工厂类
     */
    public MessageReceiver(String name, MessageReveiverFactory messageReceiverFactory) {
	this.name = name;
	this.messageReceiverFactory = messageReceiverFactory;
	// 初始化线程池
	this.executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, new ArrayBlockingQueue<Runnable>(BLOCKINGQUEUE_LENGTH),
		new ThreadPoolExecutor.CallerRunsPolicy());
	logger.info(String.format("[MessageReceiver] 接收者[%s]被初始化", name));
    }

    public void run() {
	try {
	    receive();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * 执行消息接收任务
     * 
     * @throws Exception
     */
    public void receive() throws Exception {
	try {
	    Connection conn = getConnection();
	    Channel channel = conn.createChannel();
	    String exchangeName = getExchangeName();
	    channel.exchangeDeclare(getExchangeName(), "topic", false);
	    String queueName = channel.queueDeclare().getQueue();
	    channel.queueBind(queueName, exchangeName, "ysl.#");
	    logger.info(String.format("[%s] Waiting for messages.", Thread.currentThread().getName()));
	    QueueingConsumer consumer = new QueueingConsumer(channel);
	    channel.basicConsume(queueName, false, consumer);
	    while (true) {
		logger.info(String.format("[MessageReceiver] receive, running..."));
		Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody(), "UTF-8");
		logger.info(String.format("Thread:%s, name:%s, received:%s", Thread.currentThread().getName(), getName(), message));
		messageHandle(message);
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	    }
	} catch (Throwable t) {
	    logger.info(String.format("[MessageReceiver] receive, exception=%s", t.getMessage()));
	    t.printStackTrace();
	}
    }

    /**
     * 处理消息
     * 
     * @param message
     */
    private void messageHandle(String message) {
	String messageType = extractMeesageType(message);
	if (StringUtils.isBlank(messageType)) {
	    logger.info(String.format("[MessageReceiver] 消息转换失败，message=%s", message));
	    return;
	}
	MessageHandler handler = messageReceiverFactory.getHandler(messageType);
	if (handler == null) {
	    logger.info(String.format("[MessageReceiver] 获取Handler失败 messageType=%s", messageType));
	    return;
	}
	handler.setMessage(message);
	this.executorService.submit(handler);
    }

    /**
     * 从上报的消息中提取消息类型
     * 
     * @param message
     * @return
     */
    private String extractMeesageType(String message) {
	try {
	    JSONObject jobj = JSONObject.fromObject(message);
	    return jobj.optString("messageType");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 获取RabbitMQ服务器的配置
     * 
     * @return
     */
    private RabbitMQConfig getConfig() {
	RabbitMQConfig mqConfig = SpringBeanUtils.getBean("rabbitMQConfig");
	logger.info(String.format("[MessageReceiver]  getConfig, rabbitMQConfig=%s", mqConfig.toString()));
	return mqConfig;
    }

    private String getExchangeName() {
	RabbitMQConfig config = getConfig();
	String exchangeName = "fanout-exchange";
	if (config != null) {
	    String defaultExchangeName = config.getExchangeName();
	    if (!StringUtils.isEmpty(defaultExchangeName)) {
		return defaultExchangeName;
	    }
	}
	return exchangeName;
    }

    /**
     * 获取RabbitMQ连接
     * 
     * @return
     * @throws IOException
     */
    private Connection getConnection() throws IOException {
	ConnectionFactory factory = new ConnectionFactory();
	RabbitMQConfig mqConfig = getConfig();
	factory.setHost(mqConfig.getHost());
	factory.setPort(mqConfig.getPort());
	// 不设置用户名和密码会报权限异常
	factory.setUsername(mqConfig.getUsername());
	factory.setPassword(mqConfig.getPassword());
	Connection conn = factory.newConnection();
	return conn;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /** 线程池初始线程数 */
    public static final int CORE_POOL_SIZE = 20;

    /** 线程池最大线程数 */
    public static final int MAXIMUM_POOL_SIZE = 40;

    /** 超出初始线程数的线程IDLE的时间 */
    public static final long KEEP_ALIVE_TIME = 3;

    /** 时间单位 */
    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    /** 线程队列长度 */
    public static final int BLOCKINGQUEUE_LENGTH = 80;
}
