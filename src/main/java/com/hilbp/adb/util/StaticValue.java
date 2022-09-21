package com.hilbp.adb.util;

public class StaticValue {
	
	// type
	public final static String TYPE_BACK = "back"; // 返回
	public final static String TYPE_ClICK = "click"; // 点击
	public final static String TYPE_CLICK_MATCH_NODE = "clickMatchNode"; // 通过截屏匹配目标控件位置并点击
	public final static String TYPE_CLICK_TARGET_NODE =  "clickTargetNode"; // 通过ui搜索并点击目标node的坐标
	public final static String TYPE_CUSTOM = "custom"; // 自定义复杂操作
	public final static String TYPE_DOUBLE_CLICK_TARGET_NODE =  "doubleClick"; // 双击
	public final static String TYPE_KEYEVENT =  "keyevent"; // 按键
	public final static String TYPE_LAST_PAGE =  "lastPage"; // 是否是最后一页
	public final static String TYPE_MANUAL_CONTROL = "manualControl"; //pc操控手机
	public final static String TYPE_OPEN = "open"; // 打开app
	public final static String TYPE_SAVE_MATCH_NODE = "saveMatchNode"; // 通过截屏匹配目标控件位置并保存
	public final static String TYPE_SAVE_TARGET_NODE = "saveTargetNode"; // 通过ui搜索并将目标node的坐标保存在数组中
	public final static String TYPE_SET_IME = "setIme"; // 设置输入法
	public final static String TYPE_SET_MSG_TO_INPUT = "setTextToInput"; // 在input中设置文本
	public final static String TYPE_SWIPE = "swipe"; // 滑动
	

	// status
	public final static String STATUS_INIT = "init"; // 初始化，只执行一次
	public final static String STATUS_RUNNING = "running"; // 循环执行
	public final static String STATUS_FINISHED = "finished"; // 操作完成，只执行一次
	public final static String STATUS_EXCEPTION = "exception"; // 发生异常时执行一次做清理
	
	// direction when type=swipe
	public final static String DIRECTION_UP = "up"; 
	public final static String DIRECTION_DOWN = "down"; 
	public final static String DIRECTION_LEFT = "left"; 
	public final static String DIRECTION_RIGHT = "right"; 
	
	// 提示消息
	public final static String MSG_COORD_EMPTY = "未找到目标node的坐标";
	public final static String MSG_TEXT_EMPTY = "你在说什么啊？";
	
	//socket 订阅
	public final static String SOCKET_PUSH_LOGGER = "/topic/logger"; //日志推送到前端
	public final static String SOCKET_SCREEN_MIRROR = "/topic/screen"; //投屏
	public final static String SOCKET_SCREEN_VIDEO = "/topic/video"; //投屏

	//action state状态类型
	public final static String ACTION_STATE_TYPE_COORDS = "coords";
	public final static String ACTION_STATE_TYPE_NODES = "nodes";
	public final static String ACTION_STATE_TYPE_MANUAL = "manual"; //手动操控

	//pc操作手机时的操作类型
	public final static String MANUAL_TYPE_CLICK = "click"; //点击屏幕
	public final static String MANUAL_TYPE_INPUT = "input"; //输入文字
	public final static String MANUAL_TYPE_SWIPE = "swipe"; //滑动屏幕
	public final static String MANUAL_TYPE_KEY = "key"; //按键
	
}
