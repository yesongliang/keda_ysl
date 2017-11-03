package com.kedacom.ysl.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiverTest {
    private final static String EXCHANGE_NAME = "topic-ysl";

    public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException {
	// 创建连接和频道
	ConnectionFactory factory = new ConnectionFactory();
	factory.setUsername("admin");
	factory.setPassword("admin");
	factory.setHost("172.16.48.64");
	Connection connection = factory.newConnection();
	Channel channel = connection.createChannel();

	channel.exchangeDeclare(EXCHANGE_NAME, "topic",false);
	// 创建一个非持久的、唯一的且自动删除的队列
	String queueName = channel.queueDeclare().getQueue();
	// 为转发器指定队列，设置binding
	channel.queueBind(queueName, EXCHANGE_NAME, "ysl.#");

	System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	QueueingConsumer consumer = new QueueingConsumer(channel);
	// 指定接收者，第二个参数为自动应答，无需手动应答
	channel.basicConsume(queueName, true, consumer);

	while (true) {

	    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	    String message = new String(delivery.getBody(), "UTF-8");
	    System.out.println(" [x] Received '" + message + "'");
	}
    }
}
