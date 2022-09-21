package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Node;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

/**
 * @desc 有两个作用，将其设置到input中：
 * 1.向文本设置自定义用语 action-state-name = null
 * 2.从actionState获取源文本调用语义接口返回的文本 action-state-name != null
 * @author hilbp
 *
 */

@Component
@Slf4j
public class SetTextToInput extends AbstractActionType {

	public interface TextSource {
		String get(JadbDevice device);
		String get(JadbDevice device, String input);
	}

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SaveActionState saveActionState;
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {

		typeExecuteUtil.beforExecuteShell(device, action);
		String textSourceBeanName = action.getTextSourceBeanName();
		if(StringUtil.isEmpty(textSourceBeanName))
			throw new RuntimeException("SetTextToInput err: 无文本来源！");

		TextSource textSource = (TextSource) applicationContext.getBean(textSourceBeanName);
		String actionStateName = action.getActionStateName();

		String msg = StringUtil.isEmpty(actionStateName) == true ? textSource.get(device) : textSource.get(device, getMsgFromState(device, actionStateName));

		log.info("msg: {}", msg);
		adbShellUtil.sendMessage(device, msg);
		typeExecuteUtil.afterExecuteShell(device, action);
	}

	//从保存的状态中获取文本
	protected String getMsgFromState(JadbDevice device, String actionStateName) {
		
		List<Node> nodes = saveActionState.getStateData(device, actionStateName);
		if(nodes.size() == 0) {
			throw new RuntimeException("getMsgFromState err: 从state中获取消息失败！");
		}
		String message = nodes.get(nodes.size() - 1).getText();
		log.info("→   识别内容：{}", message);
		return message;
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}
	
}
