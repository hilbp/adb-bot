package com.hilbp.adb.action.type;

import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import se.vidstige.jadb.JadbDevice;

public interface IActionType {

	//调用operate
	void start(JadbDevice device, Action action);
	void start(JadbDevice device, Action action, ActionResult resutl);

}
