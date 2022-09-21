package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.util.StaticValue;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import se.vidstige.jadb.JadbDevice;

@Component
//@Slf4j
public class DoubleClick extends AbstractActionType {
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
				
		typeExecuteUtil.beforExecuteShell(device, action);
		
		//如果父action给子action传递的坐标，优先执行子action的，二者选其一
		Coord coord = action.getLocation();
		if(coord == null) {
			coord = action.getParentToChildLocation();
		}
		Assert.isTrue(coord != null, action.getName() + StaticValue.MSG_COORD_EMPTY);
		
		adbShellUtil.doubleClick(device, coord.getX(), coord.getY());
		typeExecuteUtil.afterExecuteShell(device, action);
			
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}

}
