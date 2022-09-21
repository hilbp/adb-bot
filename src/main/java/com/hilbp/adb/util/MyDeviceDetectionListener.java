package com.hilbp.adb.util;

import java.util.List;

import se.vidstige.jadb.DeviceDetectionListener;
import se.vidstige.jadb.JadbDevice;

public class MyDeviceDetectionListener implements DeviceDetectionListener {
	
	public List<JadbDevice> devices;
	
	@Override
	public void onDetect(List<JadbDevice> devices) {
		
		this.devices = devices;
	}

	@Override
	public void onException(Exception e) {
		
		System.out.println("DeviceDetectionListener Exception");
	}

}
