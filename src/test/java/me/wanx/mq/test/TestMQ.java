package me.wanx.mq.test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import me.wanx.mq.QueueUtils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestMQ {
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-active-mq.xml");
		send2(ctx);
	}
	
	public static void send2(ApplicationContext ctx){
		QueueUtils u = (QueueUtils)ctx.getBean("queueUtils");
		u.send2Queue("demo1", new foo(1,"foo"));
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
	
	public static void hoo(){
		QueueUtils u = new QueueUtils();
		String s = u.ObjectToJson(new foo(1,"foo"));
		System.out.println(s);
	}
	
}

class foo{
	private int i;
	private String j;
	public foo(){}
	public foo(int i ,String j){
		this.i = i;
		this.j = j;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public String getJ() {
		return j;
	}
	public void setJ(String j) {
		this.j = j;
	}
	
}
