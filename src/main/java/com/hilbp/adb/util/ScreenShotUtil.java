package com.hilbp.adb.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

/**
 * adb截屏
 * @author hilbp
 *
 */
public class ScreenShotUtil {


   
	//手机截屏并上传到电脑
	public static void getScreenshot(JadbDevice device, String savePath) {
		try {
			device.executeShell("screencap -p /data/local/tmp/screen.png");
			Thread.sleep(3000);
			RemoteFile remoteFile = new RemoteFile("/data/local/tmp/screen.png");
			File file = new File(savePath);
			device.pull(remoteFile, file);
	        long oldLength;
			do {
				oldLength = file.length();
				Thread.sleep(1000);
			}while(oldLength != file.length());
		} catch (IOException | JadbException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void getScreenshot(JadbDevice device, String savePath, boolean bool) {
    	try {
    		String cmd = "adb exec-out screencap -p > " + savePath;
        	String [] command = {"cmd" , "/C" , cmd};
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getScreenshot(JadbDevice device) {
    	try {
    		String cmd = "adb exec-out screencap -p";
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream inputStream = process.getInputStream();
			
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
			byte[] buff = new byte[2048];
			int rc = 0; 

			while ((rc = inputStream.read(buff, 0, 100)) > 0) { 
				swapStream.write(buff, 0, rc); 
			} 
			byte[] bin = swapStream.toByteArray();
			return bin;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	return null;
	}

}