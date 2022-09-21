package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.StaticValue;
import com.hilbp.adb.util.UiAutoMatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

/**
 * 从布局文件获取目标node并保存
 * @author hilbp
 *
 */
@Component
public class SaveTargetNode extends AbstractActionType {
	
	@Autowired
	private SaveActionState saveActionState;

	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
		
		typeExecuteUtil.beforExecuteShell(device, action);
		adbShellUtil.saveUiFile(device, action.getUiSavePath());
		typeExecuteUtil.afterExecuteShell(device, action);
		
		//保存状态：新的
		saveActionState.saveState(device, action, StaticValue.ACTION_STATE_TYPE_NODES , UiAutoMatorUtil.getTargetNode(action));
		
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}
}
