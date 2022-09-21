package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import se.vidstige.jadb.JadbDevice;

@Component
public class Back extends AbstractActionType {

	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
		
		typeExecuteUtil.beforExecuteShell(device, action);
		int i = 0;
		while(i < 5) {
			adbShellUtil.back(device);
			if(adbShellUtil.isTargetActivity(device, action.getTargetActivity())) {
				return;
			}
			i++;
		}
		Assert.isTrue(i >= 5, "返回失败");
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}
}
