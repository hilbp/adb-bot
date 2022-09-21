package com.hilbp.sample.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "test", url = "http://api.qingyunke.com/api.php")
public interface SimpleBotClientService {
	
	/**
	 * 一个简单的机器人聊天接口
	 * @param key
	 * @param appid
	 * @param msg
	 * @return
	 */
//	 @Headers({"Content-Type: application/json","Accept: application/json"})
	@RequestMapping(method = RequestMethod.GET, value = "/")
	String chatBot(
    		@RequestParam("key") String key,
    		@RequestParam("appid") int appid,
    		@RequestParam("msg") String msg);
	
	

}
