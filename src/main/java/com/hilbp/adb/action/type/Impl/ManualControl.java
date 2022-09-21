package com.hilbp.adb.action.type.Impl;

import com.alibaba.fastjson.JSON;
import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.entity.ManualControlEntity;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.StaticValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

/**
 * 手动操作屏幕
 * 1.支持手动点击
 * 2.支持发送文字
 * @author hilbp
 *
 */
@Component
@Slf4j
public class ManualControl extends AbstractActionType {

	@Autowired
	private SaveActionState saveActionState;
	
	@Override
	protected void operate(JadbDevice device, Action action) {
		this.run(device, action);
	}

	protected void run(JadbDevice device, Action action) {
		ManualControlEntity manualControlEntity = saveActionState.getStateData(device, action.getActionStateName());
		if(manualControlEntity.isOperated()) {
			switch(manualControlEntity.getType()) {
				case StaticValue.MANUAL_TYPE_CLICK:
					Coord coord = manualControlEntity.getCoord();
					adbShellUtil.click(device, coord.getX(), coord.getY());
					break;
				case StaticValue.MANUAL_TYPE_INPUT:
					String msg = manualControlEntity.getText();
					String defaultIme = "com.sohu.inputmethod.sogou/.SogouIME";
					String adbIme = "com.android.adbkeyboard/.AdbIME";
					adbShellUtil.setIme(device, adbIme);
					adbShellUtil.sendMessage(device, msg);
					adbShellUtil.setIme(device, defaultIme);
					break;
				case StaticValue.MANUAL_TYPE_SWIPE:
					this.swipe(device, manualControlEntity.getCoord(), manualControlEntity.getOffset());
					break;
				case StaticValue.MANUAL_TYPE_KEY:
					log.info("manualControlEntity: {}", JSON.toJSON(manualControlEntity));
					adbShellUtil.keyEvent(device, manualControlEntity.getKeycode());
					break;
			}
			manualControlEntity.setOperated(false);
		}
	}

	protected void swipe(JadbDevice device, Coord start, Coord offset) {
		int x2 = start.getX() + offset.getX();
		int	y2 = start.getY() + offset.getY();
		Coord end = new Coord();
		end.setX(x2);
		end.setY(y2);
		adbShellUtil.swipe(device, start, end, 100);
	}

	@Override
	protected void operate(JadbDevice device, Action action, ActionResult resutl) {
		// TODO Auto-generated method stub
	}
}
