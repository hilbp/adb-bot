package com.hilbp.adb.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import se.vidstige.jadb.JadbDevice;

@Component
@Slf4j
public class ActionTaskSchedule {
	
	@Autowired
	private ActionTask actionTask;
	
	private final static Semaphore permit = new Semaphore(1);
	private final static ExecutorService executors = Executors.newSingleThreadExecutor();
	
//	private final static ExecutorService executors = new ThreadPoolExecutor(
//	1, 
//	1,
//    0L, 
//    TimeUnit.MILLISECONDS,
//    new LinkedBlockingQueue<Runnable>(),
//    new ThreadPoolExecutor.DiscardOldestPolicy());
	
	public void start(JadbDevice device, String name) {
		
		if(permit.tryAcquire()) {
			RunningFlag.set(true);
			actionTask.setMeta(device, name);
			executors.execute(actionTask);
		} else {
			log.info("请先终止运行的任务");
		}
	}
	
	public void stop() {
		RunningFlag.set(false);
		permit.release();
		log.info("已停止");
	}
}
