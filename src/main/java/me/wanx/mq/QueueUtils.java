package me.wanx.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;

/**
 * 
* @ClassName: QueueUtils 
* @Description: JMS 工具类 
* @author gqwang
* @date 2014年11月14日 下午2:07:44 
*
 */
public class QueueUtils {
	/** jms模版 **/
	private JmsTemplate jmsTemplate;

	public void send2Queue(String queueName,final Object obj){
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(ObjectToJson(obj));
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
