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
	
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		logger.info("customer receiver message");
		if(message instanceof TextMessage){
			TextMessage tmsg = (TextMessage)message;
			logger.info("========receiver data:"+tmsg.getText());
		}
	}

}
