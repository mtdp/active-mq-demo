package me.wanx.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import me.wanx.common.core.utils.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("rawtypes")
public class Customer implements SessionAwareMessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Customer.class);
	int i = 0;
	
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		logger.info("customer receiver message time="+DateUtil.getCurrentTime());
		if(message instanceof TextMessage){
//			if(i == 0){
//				i++;
				/**
				 * http://blog.csdn.net/kimmking/article/details/19127153
				 * 处理失败时的消息重发机制
					1. 处理失败 指的是MessageListener的onMessage方法里抛出RuntimeException。
					2. Message头里有两个相关字段：Redelivered默认为false，redeliveryCounter默认为0。
					3. 消息先由broker发送给consumer，consumer调用listener，如果处理失败，
					         本地redeliveryCounter++，给broker一个特定应答，broker端的message里redeliveryCounter++，
					         延迟一点时间继续调用，默认1s。超过6次，则给broker另一个特定应答，broker就直接发送消息到DLQ。
					4. 如果失败2次，consumer重启，则broker再推过来的消息里，redeliveryCounter=2，本地只能再重试4次即会进入DLQ。
					5. 重试的特定应答发送到broker，broker即会在内存将消息的redelivered设置为true，redeliveryCounter++，
					         但是这两个字段都没有持久化，即没有修改存储中的消息记录。所以broker重启时这两个字段会被重置为默认值。
				 */
//				throw new JMSException("++++++++++++++++mq消费者测试抛出异常");
//			}
			TextMessage tmsg = (TextMessage)message;
			String jsonString = tmsg.getText();
			logger.info("************receiver data string:"+jsonString);
			//json转换成obj
			MQDemo demo = JSON.parseObject(jsonString,MQDemo.class);
			logger.info("************receiver data demo:"+demo);
		}else{
			logger.error("************非文本消息message="+message);
		}
	}
	
//	@Override
//	public void onMessage(Message message, Session session) throws JMSException {
//		logger.info("customer receiver message");
//		//设计异常看是否重试
//		while(true){
//			i++;
//			if(i == 1){
//				throw new JMSException("故意报错!");
//			}
//		if(message instanceof TextMessage){
//			TextMessage tmsg = (TextMessage)message;
//			String jsonString = tmsg.getText();
//			logger.info("========receiver data string:"+jsonString);
//			//json转换成obj
//			MQDemo demo = JSON.parseObject(jsonString,MQDemo.class);
//			Object obj = JSON.parse(jsonString);
//			logger.info("========receiver data obj:"+obj);
//				break;
//		}
//		}
//	}

}
