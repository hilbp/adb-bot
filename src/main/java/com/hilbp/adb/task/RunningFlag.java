package com.hilbp.adb.task;

import org.springframework.stereotype.Component;

@Component
public class RunningFlag {
	
	private static boolean isRunning = true;

	public static boolean get() {
		return isRunning;
	}
	
	public static void set(boolean isRunning) {
		RunningFlag.isRunning = isRunning;
	}
	
}
