package com.hilbp.adb.action;

import java.util.List;

import com.hilbp.adb.entity.Action;

import se.vidstige.jadb.JadbDevice;

public interface ActionSchedule {

	void run(JadbDevice device, List<Action> actions);
}
