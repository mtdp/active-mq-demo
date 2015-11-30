package me.wanx.hornetq.monitor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 监控hornetq队列中消息的数量
* @ClassName: MonitorHornetqMessage 
* @Description: TODO 
* @author gqwang
* @date 2015年1月23日 上午10:16:27 
*
 */
@Service
public class MonitorHornetqMessage {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorHornetqMessage.class);
	
	private static String MODULE_CORE = "org.hornetq:module=Core";
	
	private static String MODULE_JMS = "org.hornetq:module=JMS";
	
	private String host;
	private int port;
	private JMXConnector connect;
	
	/** 保存队列消息数量数据 **/
	private Map<String,List<Long>> map = new HashMap<String,List<Long>>();
	
	public MonitorHornetqMessage(){
		new MonitorHornetqMessage("10.48.171.177", 4000);
	}
	
	public MonitorHornetqMessage(String host,int port){
		this.host = host;
		this.port = port;
		//连接hornetq队列
		try {
			JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+this.host+":"+this.port+"/jmxrmi");
			JMXConnector connect = JMXConnectorFactory.connect(serviceURL);
			this.connect = connect;
		} catch (MalformedURLException e) {
			logger.error("连接hornetq队列出错",e);
		} catch (IOException e) {
			logger.error("连接hornetq队列IO出错",e);
		}
	}
	
	/**
	 * 处理
	 */
	public void process(){
		MBeanServerConnection mBean;
		try {
			mBean = connect.getMBeanServerConnection();
			
			logger.info(ToStringBuilder.reflectionToString(mBean));
			
			Set<ObjectName> set = mBean.queryNames(null, null);
			for(ObjectName objName : set){
				String type = objName.getKeyProperty("type");
				String name = objName.getKeyProperty("name");
				String module = objName.toString();
				//过滤是队列且org.hornetq:module=JMS的信息
//				if("Queue".equals(type) && module.indexOf(MonitorHornetqMessage.MODULE_JMS) > -1 && name != null && name.indexOf("sgs") > -1){
				if("Queue".equals(type) && module.indexOf(MonitorHornetqMessage.MODULE_JMS) > -1 && name != null && name.indexOf("supply") > -1){
					//DeliveringCount MessagesAdded ConsumerCount
					Object value = mBean.getAttribute(objName, "ConsumerCount");
					Object value1 = mBean.getAttribute(objName, "MessagesAdded");
					Object value2 = mBean.getAttribute(objName, "DeliveringCount");
//					if(((Integer)value) > 0){
						logger.info("objectName="+objName+"|ConsumerCount==>"+value+"|MessagesAdded==>"+value1+"|DeliveringCount==>"+value2);
//					}
//					if(map.containsKey(name)){
//						List<Long> values = map.get(name);
//						values.add(value);
//					}else{
//						List<Long> lists = new ArrayList<Long>();
//						map.put(name,lists);
//					}
				}
			}
		} catch (IOException e) {
			logger.error("获取hornetq队列属性出错",e);
		} catch (AttributeNotFoundException e) {
			logger.error("获取hornetq队列属性出错",e);
		} catch (InstanceNotFoundException e) {
			logger.error("获取hornetq队列属性出错",e);
		} catch (MBeanException e) {
			logger.error("获取hornetq MBeanException 出错",e);
		} catch (ReflectionException e) {
			logger.error("获取hornetq队列属性出错",e);
		}
		
	}
	
	public static void main(String[] args) {
		MonitorHornetqMessage monitor = new MonitorHornetqMessage("10.48.171.177", 4000);
		monitor.process();
	}

}
