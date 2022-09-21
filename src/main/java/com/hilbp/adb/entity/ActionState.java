package com.hilbp.adb.entity;

import lombok.Data;

@Data
public class ActionState<T> {
	
	private Action action;
	private String type;
	private T data;
}
