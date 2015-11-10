package me.wanx.mq;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("me.wanx.mq.Provider")
public class Provider {
	
	private static final Logger logger = LoggerFactory.getLogger(Provider.class);
	
	@Autowired
	@Qualifier("queueUtils")
	private QueueUtils queueUtils;
	
	private Random random = new Random();
	
	//线程池
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 40,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(100));
		
	
	@Scheduled(cron="${provider.cron}")
	public void produce(){
		logger.info("*-*-*-*-*-*发送消息");
		int idx = 0;
		while(idx ++ < 10){
			this.executorService.submit(new Runnable() {
				@Override
				public void run() {
					int r = random.nextInt(100000);
					queueUtils.send2Queue("demo1",new MQDemo(String.valueOf(r), "name:"+r) );
				}
			});
		}
	}

}
