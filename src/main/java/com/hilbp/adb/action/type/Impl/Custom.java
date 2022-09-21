package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

@Component
//@Slf4j
public class Custom extends AbstractActionType {

	public interface CustomOperate {
		void operate(JadbDevice device, Action action);
	}
	
	@Autowired
	ApplicationContext applicationContext;

	@Override
	protected void operate(JadbDevice device, Action action) {

		typeExecuteUtil.beforExecuteShell(device, action);
		CustomOperate customOperate = (CustomOperate) applicationContext.getBean(action.getCustomOperateBeanName());
		customOperate.operate(device, action);
		typeExecuteUtil.afterExecuteShell(device, action);
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}

}
