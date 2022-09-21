package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

@Component
public class Keyevent extends AbstractActionType {

	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {

		typeExecuteUtil.beforExecuteShell(device, action);
		adbShellUtil.keyEvent(device, action.getKeyCode());
		typeExecuteUtil.afterExecuteShell(device, action);
			
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}
}
