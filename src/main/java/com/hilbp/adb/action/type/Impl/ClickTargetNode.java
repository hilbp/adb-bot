package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.util.UiAutoMatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

/**
 * @desc 通过布局文件获取坐标，然后点击
 * @author hilbp
 */
@Component
@Slf4j
public class ClickTargetNode extends AbstractActionType {
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {

		//获取ui文件
		if(action.getIsNotGetNewUi() == null || !action.getIsNotGetNewUi()) {
			typeExecuteUtil.beforExecuteShell(device, action);
			adbShellUtil.saveUiFile(device, action.getUiSavePath());
			typeExecuteUtil.afterExecuteShell(device, action);
		}

		//通过xpath搜索坐标位置集合
		List<Coord> coords = UiAutoMatorUtil.getTargetCoord(action);
		log.info("ClickTargetNode coords: {}", coords);
		//进行点击操作
		typeExecuteUtil.click(device, action, coords);
	}
	
	
	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {}

}
