package com.hilbp.adb.action.type.Impl;

import com.alibaba.fastjson.JSON;
import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

/**
 * 投屏
 * @author hilbp
 *
 */
@Component
@Slf4j
public class ScreenMirror extends AbstractActionType {
	
//	@Autowired
//    private SimpMessagingTemplate messagingTemplate;
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
		long start = System.currentTimeMillis();
//		adbShellUtil.getScreenshotBase64(device, messagingTemplate);
		log.info("time: {}", System.currentTimeMillis() - start);

	}

	protected void run1(JadbDevice device, Action action) {
		long start = System.currentTimeMillis();
//		byte[] bin = adbShellUtil.getScreenshot(device);
//		String jsonStr = JSON.toJSONString(bin);
		String base64Str = adbShellUtil.getScreenshotBase64(device);
//		String jsonStr = JSON.toJSONString(base64Str);
		log.info("time: {}", System.currentTimeMillis() - start);
//		messagingTemplate.convertAndSend(StaticValue.SOCKET_SCREEN_MIRROR, base64Str);

	}

	protected void runNewThread(JadbDevice device, Action action) {

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				long start = System.currentTimeMillis();
//				byte[] bin = adbShellUtil.getScreenshot(device);
//				String jsonStr = JSON.toJSONString(bin);
				String base64Str = adbShellUtil.getScreenshotBase64(device);
				String jsonStr = JSON.toJSONString(base64Str);
//				messagingTemplate.convertAndSend(StaticValue.SOCKET_SCREEN_MIRROR, jsonStr);
				log.info("time: {}", System.currentTimeMillis() - start);
			}
		});
		thread.start();
	}



	@Override
	public void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}

}
