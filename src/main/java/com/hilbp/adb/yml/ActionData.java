package com.hilbp.adb.yml;

import java.util.List;

import com.hilbp.adb.entity.Action;

import lombok.Data;


@Data
public class ActionData {
	
	private String name;
	private String desc; 
	private String title;
	private List<Action> actions;

}
