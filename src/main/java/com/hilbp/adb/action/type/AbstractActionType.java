package com.hilbp.adb.action.type;

import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.util.AdbShellUtil;
import com.hilbp.adb.util.TypeExecuteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import se.vidstige.jadb.JadbDevice;

//@Slf4j
public abstract class AbstractActionType implements IActionType {
	
	protected ThreadLocal<Object> target = new ThreadLocal<Object>();

	@Autowired
	protected AdbShellUtil adbShellUtil;
	
	@Autowired
	protected TypeExecuteUtil typeExecuteUtil;

	public void start(JadbDevice device, Action action) {
		adbShellUtil.setAfterActionWaitMillisecond(action.getAfterActionWaitMillisecond());
		operate(device, action);
	}

	public void start(JadbDevice device, Action action, ActionResult resutl) {
		adbShellUtil.setAfterActionWaitMillisecond(action.getAfterActionWaitMillisecond());
		operate(device, action, resutl);
	}

	//具体action的操作
	protected abstract void operate(JadbDevice device, Action action);
	protected abstract void operate(JadbDevice device, Action action, ActionResult resutl);


	
	

	
}
