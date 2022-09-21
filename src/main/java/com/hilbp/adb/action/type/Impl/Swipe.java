package com.hilbp.adb.action.type.Impl;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.util.StaticValue;
import com.hilbp.adb.util.UiAutoMatorUtil;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

@Component
public class Swipe extends AbstractActionType {
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {

		Coord offset = action.getOffset();
		Coord start = action.getLocation();
		
		//起始坐标设置随机偏移量
		start.setX(start.getX() + UiAutoMatorUtil.getRandom(5, -5));
		start.setY(start.getY() + UiAutoMatorUtil.getRandom(5, -5));

		int x1 = start.getX();
		int y1 = start.getY();
		int x2, y2;
		
		switch(action.getDirection()) {
			case StaticValue.DIRECTION_UP:
				x2 = x1;
				y2 = y1 - offset.getY();
				break;
			case StaticValue.DIRECTION_DOWN:
				x2 = x1;
				y2 = y1 + offset.getY();
				break;
			case StaticValue.DIRECTION_LEFT:
				x2 = x1 - offset.getX();
				y2 = y1;
				break;
			case StaticValue.DIRECTION_RIGHT:
				x2 = x1 + offset.getX();
				y2 = y1;
				break;
			default:
				System.out.println("direction not exist");
				return;
		}
		
		Coord end = new Coord();
		end.setX(x2);
		end.setY(y2);
		
		typeExecuteUtil.beforExecuteShell(device, action);
		adbShellUtil.swipe(device, start, end, 100);
		typeExecuteUtil.afterExecuteShell(device, action);
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
		
	}

}
