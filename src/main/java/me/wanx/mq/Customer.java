package me.wanx.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

public class Customer implements SessionAwareMessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Customer.class);
	int i = 0;
	
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		logger.info("customer receiver message");
		//设计异常看是否重试
		while(true){
			i++;
			if(i == 1){
				throw new JMSException("故意报错!");
			}
			if(message instanceof TextMessage){
				logger.info("正常");
				TextMessage tmsg = (TextMessage)message;
				logger.info("========receiver data:"+tmsg.getText());
				break;
			}
		}
	}

}
