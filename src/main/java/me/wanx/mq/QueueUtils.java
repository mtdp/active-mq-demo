package me.wanx.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;

/**
 * 
* @ClassName: QueueUtils 
* @Description: JMS 工具类 
* @author gqwang
* @date 2014年11月14日 下午2:07:44 
* javax.jms 包在jdk1.5文档中
 */
public class QueueUtils {
	/** jms模版 **/
	private JmsTemplate jmsTemplate;

	/**
	 * 发送格式为json的消息
	 * @param queueName
	 * @param obj
	 */
	public void send2Queue(String queueName,final Object obj){
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(ObjectToJson(obj));
			}
		});
	}
	
	/**
	 * 延时发送格式为json的消息
	 * @param queueName
	 * @param obj
	 */
	public void sendDelay2Queue(String queueName,final Object obj){
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(ObjectToJson(obj));
				//延时时间 毫秒 
				Long time = 10*1000L;
				//要添加activeMQ的activemq.xml配置文件broker节点属性schedulerSupport="true" 才可以生效
				message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
				return message;
			}
		});
	}
	
	public String ObjectToJson(Object obj){
		String result = "";
		result = JSON.toJSONString(obj);
		return result;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
}
