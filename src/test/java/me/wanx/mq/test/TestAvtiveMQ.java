package me.wanx.mq.test;

import java.util.Date;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import me.wanx.common.core.utils.DateUtil;
import me.wanx.mq.MQDemo;
import me.wanx.mq.QueueUtils;
import me.wanx.mq.test.bean.Foo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestAvtiveMQ {
	public static void main(String[] args) throws InterruptedException {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-active-mq.xml");
		/*int i = 0;
		while(i < 1){
			i++;
			send2(ctx);
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		send2Delay(ctx);
		System.out.println(DateUtil.getCurrentTime());
		Thread.currentThread().join();
	}
	
	public static void send2(ApplicationContext ctx){
		QueueUtils u = (QueueUtils)ctx.getBean("queueUtils");
		u.send2Queue("demo1", new Foo(new Random().nextInt(),"foo"));
	}
	
	public static void send1(ApplicationContext ctx){
		JmsTemplate jms = (JmsTemplate)ctx.getBean("jmsTemplate");
		final String msg = "this is test";
		jms.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
		Message m = jms.receive();
		System.out.println(ToStringBuilder.reflectionToString(m));
	}
	
	private static void send2Delay(ApplicationContext ctx){
		QueueUtils u = (QueueUtils)ctx.getBean("queueUtils");
		Random random = new Random();
		int r = random.nextInt(100000);
		u.sendDelay2Queue("demo1", new MQDemo(String.valueOf(r), "name:"+r));
	}
	
	public static void hoo(){
		QueueUtils u = new QueueUtils();
		String s = u.ObjectToJson(new Foo(1,"foo"));
		System.out.println(s);
	}
	
}

