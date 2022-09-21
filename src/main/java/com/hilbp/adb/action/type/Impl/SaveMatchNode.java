package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.MatchTemplateUtil;
import com.hilbp.adb.util.StaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.List;

/**
 * 截屏，然后根据控件图标利用javacv匹配图标
 * 1.截屏的保存位置
 * 2.目标图的保存位置
 * 3.匹配结果的保存位置
 * 4.阈值设置
 * @author hilbp
 *
 */
@Component
public class SaveMatchNode extends AbstractActionType {
	
	@Autowired
	private SaveActionState saveActionState;
	
	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {}
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
		String sourcePath = action.getSourcePath();
		
		//先截屏
		typeExecuteUtil.beforExecuteShell(device, action);
		adbShellUtil.getScreenshot(device, sourcePath);
		typeExecuteUtil.afterExecuteShell(device, action);
		
		//保存状态
		String templatePath = action.getTemplatePath();
		List<Coord> coords = MatchTemplateUtil.match(sourcePath, templatePath);
		saveActionState.saveState(device, action, StaticValue.ACTION_STATE_TYPE_COORDS , coords);
	}

	
}
