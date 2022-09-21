# app-bot

使用过类似于appium的框架，但感觉上手很繁琐，运行速度慢。所以基于adb使用java实现了基本的模拟操作。可基于此实现许多有意思的模拟操作。

### 功能

  - 微信公众号检索及内容查看
  - soul：灵魂匹配、机器人聊天、对瞬间点赞、对瞬间评论
  - 抖音：对视频的评论进行点赞

> 对瞬间评论：提取瞬间内容，调用AI接口进行语义识别，然后调用机器接口获取应答内容，之后评论
>
> 机器人聊天：获取对方消息内容，调用机器人接口语义识别且回复

### 环境要求

  - 电脑端OS：目前仅支持windows平台，已在win11测试正常
  - java：17
  - adb：version 1.0.41
  - 手机OS：安卓，无需root，需打开调试模式
  
### 一些action类介绍：

action是一次模拟人行为的操作。支持的action如代码注释：
```java
//package com.hilbp.adb.entity
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
```

基于该action配置操作流程的yml文件，使用调度器来顺序执行这些流程。如下是yml中一个action的说明：
```yaml

#`ClickTargetNode`：获取布局文件搜索目标node，保存在list中，遍历list对每个node进行点击操作。当前点击出现异常时，点击下一个符合条件的node。适用于同一个页面操作，不适合点击后页面发生跳转的操作。需要传递的参数示例：

- order: 3
- name: 点击文本框获取输入焦点
- status: running
- type: clickTargetNode
- not-get-new-ui: false #是否获取新的ui布局文件，根据上下文决定，对响应时间有一定的影响
- ui-save-path: E:/adb/uidump.xml
- xpath: //node[@resource-id='cn.soulapp.android:id/et_sendmessage']
- current-activity: ${config.soul.activity.chat-window-activity.name}
- target-activity: ${config.soul.activity.chat-window-activity.name}
```

### 关于点击定位来源支持：

- 在yml的action中手动配置点击坐标
- 在yml的action中配置目标元素的边界坐标（推荐）
- 获取xml格式的布局文件通过dom4j定位目标元素获取坐标
- 通过截屏使用javacv匹配目标图像获取坐标并点击

### 扩展

- 对调度器的扩展：实现`ActionSchedule`接口，并在配置文件配置配置`adbConfig.adbBotActionSchedule`的值为该bean的名称
- 对action（模拟操作）的扩展，实现`Custom.CustomOperate`接口。

### 模拟操作配置参考

`resources/configAction`中的文件。
