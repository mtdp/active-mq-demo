package me.wanx.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.wanx.hornetq.monitor.MonitorHornetqMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Scope(value = "prototype")
public class MqAction {
	private static final Logger logger = LoggerFactory.getLogger(MqAction.class);
	
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	
	private String charset = "UTF-8";
	private String contentType = "text/plain; charset=UTF-8";
	
	//每个请求都执行
	@ModelAttribute
	public void setRequestAndResponse(HttpServletRequest request,HttpServletResponse response){
		this.request = request;
		this.response = response;
	}

	@Autowired
	private MonitorHornetqMessage monitorHornetqMessage;
	
	private LinkedList<Integer> list = new LinkedList<Integer>();
	
	
	/**
	 * 初始话数据给到页面 
	 * @param limit 数量
	 */
	@RequestMapping(value="hq/initData.do",method = RequestMethod.POST)
	public @ResponseBody void initData(@RequestParam int limit){
		logger.info("开始获取初始数据");
		Random random = new Random();
		int slat = random.nextInt(100);
		//生成limit初始数量
		for(int i = 0; i < limit; i++){
			list.add(new Integer(100+i+slat));
		}
		setResultMessage4Json("success", "获取初始化数据成功", list);
	}
	
	/**
	 * 获取最新的数据
	 */
	@RequestMapping(value="hq/lastData.do",method = RequestMethod.POST)
	public @ResponseBody void lastData(){
		Random random = new Random();
		int r = random.nextInt(50);
		setResultMessage4Json("success", "获取最新的数据成功", r);
	}
	
	
	public void setResultMessage4Json(ResultMessage msg){
		//设置返回编码
		response.setContentType(this.contentType);
		response.setCharacterEncoding(this.charset);
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			logger.error("获取输出流出错!",e);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			String resultJson = mapper.writeValueAsString(msg);
			pw.write(resultJson);
			pw.flush();
		} catch (Exception e) {
			logger.error("输出json出错!",e);
		}finally{
			pw.close();
		}
	}
	
	public void setResultMessage4Json(String code,String info){
		setResultMessage4Json(new ResultMessage(code, info));
	}
	
	public void setResultMessage4Json(String code,String info,Object obj){
		setResultMessage4Json(new ResultMessage(code, info,obj));
	}
	
	class ResultMessage implements Serializable {
		private static final long serialVersionUID = -2753307512189953499L;
		
		private String code;
		private String info;
		private Object obj;
		
		public ResultMessage(){
			
		}
		
		public ResultMessage(String code ,String info){
			this.code = code;
			this.info = info;
		}
		
		public ResultMessage(String code ,String info,Object obj){
			this.code = code;
			this.info = info;
			this.obj = obj;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

	}
	
	public static void main(String[] args) {
		System.out.println(1 == 1 ? 1 : 0);
		ClassPathXmlApplicationContext cc;
	}

}
