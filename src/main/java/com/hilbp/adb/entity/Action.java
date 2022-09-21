package com.hilbp.adb.entity;

import lombok.Data;

import java.util.List;

@Data
public class Action {
	
	private int order; //序号
	private String name; //名称
	private String status; //执行状态
	private String type; //操作类型
	private String currentActivity; //当前activity
	private String targetActivity; //目标activity
	private int afterActionWaitMillisecond; //模拟操作后等待毫秒数

	//when type=keyEvent
	private int keyCode;
	
	//when type=click
	private String bounds; //可以通过边界点生成坐标来点击，与location二选一
	private Coord location; //点击的坐标位置 type=click
	private Coord parentToChildLocation; //从父action传递给子action的坐标
	
	//when type=swipe
	private Coord offset; //滑动的偏移量
	private String direction; //滑动方向：上下左右
	
	//when type=custom 反射调用
	private String customOperateBeanName; //类名
	
	//when type=setIme
	private String imeName;

	//when type=setTextToInput：往文本框发送文本
	private String textSourceBeanName;
	
	//when type=searchTargetNode：通过adb命令获取布局文件然后进行操作
	private String uiSavePath; //pull ui布局文件保存位置
	private String xpath; //布局文件中目标元素搜索
	private String actionStateName; //一个bean，用于向子action传递上下文
	private Boolean isNotGetNewUi; //是否获取新的布局文件
	
	//when type=MatchTemplate
	private String sourcePath; //原图
	private String templatePath; //目标图
	private float threshold; //阈值

	private List<Action> childActions; //一个action包含的子action集合

}
