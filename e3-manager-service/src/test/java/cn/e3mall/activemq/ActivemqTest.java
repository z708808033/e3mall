//package cn.e3mall.activemq;
//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageConsumer;
//import javax.jms.MessageListener;
//import javax.jms.MessageProducer;
//import javax.jms.Queue;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//import javax.jms.Topic;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQTextMessage;
//import org.junit.Test;
//
//public class ActivemqTest {
//	@Test
//	public void testQueueProducer() throws Exception {
//		//创建一个连接工厂对象,需要指定服务的ip及端口
//		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//		
//		//使用工厂对象创建一个Connection对象
//		Connection connection = factory.createConnection();
//		
//		//开启连接,调用Connection对象的start方法
//		connection.start();
//		
//		//创建一个Session对象
//		//第一个参数:是否开启事务,一般不开启事务.如果开启事务,则第二个参数无意义
//		//第二个参数:应答模式.一般有两种模式,自动应答和手动应答,一般使用自动应答
//		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		
//		//使用Session对象创建一个Destination对象.两种形式,queue和topic,目前测试的是queue
//		Queue queue = session.createQueue("test-queue");
//		
//		//使用Session对象创建一个Producer对象
//		MessageProducer producer = session.createProducer(queue);
//		
//		//创建一个Message对象,使用TextMessage
////		TextMessage textMessage = new ActiveMQTextMessage();
////		textMessage.setText("hello Activemq");
//		TextMessage textMessage = session.createTextMessage("hello Activemq");
//		
//		//发送消息
//		producer.send(textMessage);
//		
//		//关闭资源
//		producer.close();
//		session.close();
//		connection.close();
//	}
//	
//	@Test
//	public void testQueueConsumer() throws Exception{
//		//创建一个ConnectionFactory对象连接MQ服务器
//		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//		
//		//创建一个连接对象
//		Connection connection = connectionFactory.createConnection();
//		
//		//开启连接
//		connection.start();
//		
//		//使用Connection对象创建一个Session对象
//		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		
//		//创建一个Destination对象.queue对象
//		Queue queue = session.createQueue("spring-queue");
//		
//		//使用Session对象创建一个消费者对象
//		MessageConsumer consumer = session.createConsumer(queue);
//		
//		//接收消息
//		consumer.setMessageListener(new MessageListener() {
//			
//			@Override
//			public void onMessage(Message message) {
//				//打印结果
//				TextMessage textMessage = (TextMessage) message;
//				try {
//					String text = textMessage.getText();
//					System.out.println(text);
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		//等待接收消息
//		System.in.read();
//		
//		//关闭资源
//		consumer.close();
//		session.close();
//		connection.close();
//	}
//	
//	//topic和queue几乎一样,只是在创建Destination对象的时候有一点不同而已
//	@Test
//	public void testTopicProducer() throws Exception{
//		//创建一个连接工厂对象,需要指定服务的ip及端口
//			ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//			
//			//使用工厂对象创建一个Connection对象
//			Connection connection = factory.createConnection();
//			
//			//开启连接,调用Connection对象的start方法
//			connection.start();
//			
//			//创建一个Session对象
//			//第一个参数:是否开启事务,一般不开启事务.如果开启事务,则第二个参数无意义
//			//第二个参数:应答模式.一般有两种模式,自动应答和手动应答,一般使用自动应答
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			
//			//使用Session对象创建一个Destination对象.两种形式,queue和topic,目前测试的是topic
//			Topic topic = session.createTopic("test-topic");
//			
//			//使用Session对象创建一个Producer对象
//			MessageProducer producer = session.createProducer(topic);
//			
//			//创建一个Message对象,使用TextMessage
//			TextMessage textMessage = session.createTextMessage("hello Activemq topic");
//			
//			//发送消息
//			producer.send(textMessage);
//			
//			//关闭资源
//			producer.close();
//			session.close();
//			connection.close();
//	}
//	
//	@Test
//	public void testTopicConsumer() throws Exception{
//		//创建一个ConnectionFactory对象连接MQ服务器
//		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
//		
//		//创建一个连接对象
//		Connection connection = connectionFactory.createConnection();
//		
//		//开启连接
//		connection.start();
//		
//		//使用Connection对象创建一个Session对象
//		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		
//		//创建一个Destination对象.topic对象
//		Topic topic = session.createTopic("test-topic");
//		
//		//使用Session对象创建一个消费者对象
//		MessageConsumer consumer = session.createConsumer(topic);
//		
//		//接收消息
//		consumer.setMessageListener(new MessageListener() {
//			
//			@Override
//			public void onMessage(Message message) {
//				//打印结果
//				TextMessage textMessage = (TextMessage) message;
//				try {
//					String text = textMessage.getText();
//					System.out.println(text);
//				} catch (JMSException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		System.out.println("topic消费者C已经启动");
//		//等待接收消息
//		System.in.read();
//		
//		//关闭资源
//		consumer.close();
//		session.close();
//		connection.close();
//	}
//}
