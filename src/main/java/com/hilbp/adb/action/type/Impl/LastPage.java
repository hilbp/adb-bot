package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Node;
import com.hilbp.adb.state.RunningStatus;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.UiAutoMatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

@Component
@Slf4j
public class LastPage extends AbstractActionType {
	
	
	@Autowired
	private SaveActionState saveActionState;
	
	@Override
	protected void operate(JadbDevice device, Action action) {}
	
	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		resutl.setAction(action);
		boolean flag = this.run(device, action);
		resutl.setSuccessed(flag);
	}

	protected boolean run(JadbDevice device, Action action) {
		
		//获取ui文件
		if(action.getIsNotGetNewUi() == null || !action.getIsNotGetNewUi()) {
			typeExecuteUtil.beforExecuteShell(device, action);
			adbShellUtil.saveUiFile(device, action.getUiSavePath());
			typeExecuteUtil.afterExecuteShell(device, action);
		}
		
		String actionStateName = action.getActionStateName();
		@SuppressWarnings("unchecked")
		List<Node> nodes = (List<Node>) saveActionState.getStateData(device, actionStateName);
		
		if(nodes != null) {
			List<Node> news = UiAutoMatorUtil.getTargetNode(action);
			if(news.size() == nodes.size()) {
				if(news.size() > 0) {
					Node node1 = nodes.get(nodes.size() -1);
					Node node2 = news.get(news.size() -1);
					if(node1.getText().equals(node2.getText())) {
						log.info("→   结论：是最后一页");
						saveActionState.clearState(device, actionStateName);
						RunningStatus.setAbortSubsteps(device);
						return true;
					}
				}else {
					return true;
				}
			}
		}
		
		//保存状态
		log.info("→   结论：不是最后一页");
		saveActionState.saveState(device, action, null, null);
		return false;
	}

}
