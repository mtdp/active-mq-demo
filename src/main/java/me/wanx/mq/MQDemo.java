package me.wanx.mq;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MQDemo implements Serializable{

	private static final long serialVersionUID = -4594241641803720119L;
	
	private String demoId;
	
	private String demoName;
	
	public MQDemo(){}
	
	public MQDemo(String demoId,String demoName){
		this.demoId = demoId;
		this.demoName = demoName;
	}

	public String getDemoId() {
		return demoId;
	}

	public void setDemoId(String demoId) {
		this.demoId = demoId;
	}

	public String getDemoName() {
		return demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
