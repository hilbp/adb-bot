package com.hilbp.adb.state;

import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionState;
import com.hilbp.adb.util.StringUtil;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持保存：
 * 1.布局文件的node集合
 * 2.得到的坐标集合
 * 3.文本
 * @author hilbp
 *
 */
@Component
public class SaveActionState {
	
	protected Map<String, ActionState<Object>> state = new ConcurrentHashMap<String, ActionState<Object>>();

	public <T> T getStateData(JadbDevice device, String key) {
		try {
			key = String.format("%s_%s", device.getSerial(), key);
			return (T) state.get(key).getData();
		} catch(RuntimeException e) {
			return null;
		}
	}

	public void saveState(JadbDevice device, Action action, String type, Object data) {
		
		String actionStateName = action.getActionStateName();
		if(StringUtil.isEmpty(actionStateName)) return;
		ActionState<Object> actionState = new ActionState<Object>();
		actionState.setAction(action);
		actionState.setType(type);
		actionState.setData(data);

		String key = String.format("%s_%s", device.getSerial(), actionStateName);
		state.put(key, actionState);
	}
	
	public void clearState(JadbDevice device, String key) {

		key = String.format("%s_%s", device.getSerial(), key);
		state.put(key, null);
	}
}
