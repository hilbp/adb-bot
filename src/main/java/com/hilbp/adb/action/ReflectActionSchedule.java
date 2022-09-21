package com.hilbp.adb.action;

import com.hilbp.adb.action.type.AbstractActionType;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.ActionResult;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.entity.Node;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.task.RunningFlag;
import com.hilbp.adb.util.StaticValue;
import com.hilbp.adb.util.StringUtil;
import com.hilbp.adb.util.UiAutoMatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ReflectActionSchedule implements ActionSchedule {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private SaveActionState saveActionState;

	@SuppressWarnings("finally")
	@Override
	public void run(JadbDevice device, List<Action> actions) {
		
		while(RunningFlag.get()) {
			try {
				this.runInitAction(device, actions);
				this.runRunningAction(device, actions);
			} 
			catch(Exception e) {
				log.info("异常：{}", e.getMessage());
				this.runExceptionAction(device, actions);
			} 
			finally {
				continue;
			}
		}
	}
	
	//执行status=init状态的action（只执行一次）
	public void runInitAction(JadbDevice device, List<Action> actions) {
		
		List<Action> initActions = this.filterAction(actions, StaticValue.STATUS_INIT);
		if(initActions == null || initActions.isEmpty()) 
			return;
		
		for(Action action : initActions) {
			this.invoke(device, action);
		}
	}
	
	//执行status=running状态的action(循环执行)
	public void runRunningAction(JadbDevice device, List<Action> actions) {
		
		List<Action> runningActions = this.filterAction(actions, StaticValue.STATUS_RUNNING);
		if(runningActions == null || runningActions.isEmpty()) 
			return;
		
		while(RunningFlag.get()) {
			for(Action action : runningActions) {
				//执行父action
				this.invoke(device, action);
				//执行子action
				this.runChildAction(device, action);
			}
		}
	}
	
	//出现异常时执行
	public void runExceptionAction(JadbDevice device, List<Action> actions) {
		List<Action> exceptionActions = this.filterAction(actions, StaticValue.STATUS_EXCEPTION);
		if(exceptionActions == null || exceptionActions.isEmpty())
			return;
		for(Action action : exceptionActions) {
			this.invoke(device, action);
		}
	}
	
	//执行子action
	public void runChildAction(JadbDevice device, Action action) {
		
		List<Action> childActions = action.getChildActions();
		if(childActions == null) return;
		
		if(action.getType().equals(StaticValue.TYPE_SAVE_TARGET_NODE)) {
			this.runChildActionOfParentTypeIsSaveTargetNode(device, action, childActions);
		}
		else if(action.getType().equals(StaticValue.TYPE_CLICK_TARGET_NODE) ||
				action.getType().equals(StaticValue.TYPE_ClICK)) {
			this.runChildActionOfParentTypeIsClickTargetNode(device, childActions);
		}
		
	}
		
	//过滤action
	public List<Action> filterAction(List<Action> actions, String condition) {
		
		List<Action> temp = new ArrayList<Action>();
		for(Action action : actions) {
			if(!condition.equals(action.getStatus())) 
				continue;
			temp.add(action);
		}
		
		return temp;
	}
	
	//父action的type=saveTargetNode的子action执行
	public void runChildActionOfParentTypeIsSaveTargetNode(JadbDevice device, Action action, List<Action> childActions) {
		
		String actionStateName = action.getActionStateName();
		if(StringUtil.isEmpty(actionStateName)) return;
		List<Node> nodes = saveActionState.getStateData(device, actionStateName);
		List<Coord> coords = UiAutoMatorUtil.getCoordListFromNodes(nodes);
		for(Coord coord : coords) {
			for(Action childAction : childActions) {
				childAction.setParentToChildLocation(coord);
				this.invoke(device, childAction);
			}
		}
	}
	
	//父action的type=clickTargetNode or type=click 的子action执行
	public void runChildActionOfParentTypeIsClickTargetNode(JadbDevice device, List<Action> childActions) {
		
		int i = 3;
		outLoop: while(i > 0) {
			for(Action childAction : childActions) {
				
				if(childAction.getType().equals(StaticValue.TYPE_LAST_PAGE)) {
					boolean isLastPage = this.invoke(device, childAction, true);
					if(isLastPage) break outLoop;
					continue;
				}	
				this.invoke(device, childAction);
			}
			i--;
		}
	}
	
	//执行操作
	public void invoke(JadbDevice device, Action action) {
		log.info("current action: {}", action.getName());
		AbstractActionType type = (AbstractActionType) applicationContext.getBean(action.getType());
		type.start(device, action);
	}
	
	//执行操作(有返回值)
	public boolean invoke(JadbDevice device, Action action, boolean bool) {
		log.info("current action: {}", action.getName());
		ActionResult result = new ActionResult();
		AbstractActionType type = (AbstractActionType) applicationContext.getBean(action.getType());
		type.start(device, action, result);
		return result.isSuccessed();
	}
	
	
}
