package com.hilbp.sample.util;

import cn.xsshome.taip.nlp.TAipNlp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hilbp.adb.util.StaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @desc 自然语言相关
 * @author hilbp
 *
 */
@Component
public class NlpUtil {

	@Value("${tencent_nlp_account.app_id}")
	private String appId;

	@Value("${tencent_nlp_account.app_key}")
	private String appKey;

	@Autowired
	private ChatApiUtil chatApiUtil;

	private TAipNlp tAipNlp = new TAipNlp(appId, appKey);;
	
	public String getChatMsg(String question) {
		
		try {
			String content;
			
			if(question.length() > 280) 
				question = question.substring(0, 280);
						
			String session = new Date().getTime() / 1000 + "";
			String result = tAipNlp.nlpTextchat(session, question);
			JSONObject jsonObject = JSON.parseObject(result);
			
			int ret = jsonObject.getIntValue("ret");
			if(ret == 0) {
				
				JSONObject data = jsonObject.getJSONObject("data");
				content = data.getString("answer");
			} else
				content = chatApiUtil.getChatMsg(question);
			
			return content;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return StaticValue.MSG_TEXT_EMPTY;
	}
}
