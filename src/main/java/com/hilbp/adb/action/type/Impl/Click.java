package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.util.StaticValue;
import com.hilbp.adb.util.UiAutoMatorUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import se.vidstige.jadb.JadbDevice;

@Component
//@Slf4j
public class Click extends AbstractActionType {
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
				
		typeExecuteUtil.beforExecuteShell(device, action);

		/**
		 * 支持以下点击：
		 * 1.优先通过边界来生成坐标进行点击，如果边界为null则执行步骤2
		 * 2.如果父action给子action传递的坐标，优先执行子action的，二者选其一
		 */
		String bounds = action.getBounds();
		Coord coord = null;
		if(StringUtils.hasText(bounds)) {
			coord = UiAutoMatorUtil.boundsToCoord(bounds);
		} else {
			coord = action.getLocation();
			if(coord == null) {
				coord = action.getParentToChildLocation();
			}
		}
		Assert.isTrue(coord != null, action.getName() + StaticValue.MSG_COORD_EMPTY);

		adbShellUtil.click(device, coord.getX(), coord.getY());
		typeExecuteUtil.afterExecuteShell(device, action);
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}

}
