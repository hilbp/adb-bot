package com.hilbp.adb.entity;

import java.util.List;

import lombok.Data;

@Data
public class ActionResult {
	
	private Action action; //被执行的action信息
	private boolean isSuccessed; //action是否执行成功
	private List<Coord> coords; //坐标类型的返回结果
	private String text; //文本型的执行结果
}
