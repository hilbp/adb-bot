package com.hilbp.adb.util;

import com.google.common.io.ByteStreams;
import com.hilbp.adb.entity.Coord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;
import se.vidstige.jadb.RemoteFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public final class AdbShellUtil {

	@Value("${adbConfig.defaultAfterActionWaitMillisecond}")
	private int defaultAfterActionWaitMillisecond;

	private static int afterActionWaitMillisecond = 0;

	private JadbConnection jadb = new JadbConnection();

	private MyDeviceDetectionListener myDeviceDetectionListener = new MyDeviceDetectionListener();
	
	//获取设备列表
	public List<JadbDevice> getDevices() {
		
		List<JadbDevice> devices = null;
		try {
			devices = jadb.getDevices();
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
		return devices;	
	}
	
	public List<JadbDevice> getDeviceWatcher() {
		
		try {
			jadb.createDeviceWatcher(myDeviceDetectionListener);
		} catch (IOException | JadbException e) {
			e.printStackTrace();
		}
		List<JadbDevice> devices = myDeviceDetectionListener.devices;
		return devices;
	}
	
	//手机截屏并上传到电脑
	public void getScreenshot(JadbDevice device, String savePath) {
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
	public void getScreenshot(JadbDevice device, String savePath, boolean bool) {
    	try {
    		String cmd = "adb exec-out screencap -p > " + savePath;
        	String [] command = {"cmd" , "/C" , cmd};
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	public byte[] getScreenshot(JadbDevice device) {
    	try {
    		String cmd = "adb exec-out screencap -p";
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream inputStream = process.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
			byte[] buff = new byte[8192];
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 8192)) > 0) {
				swapStream.write(buff, 0, rc); 
			}
			inputStream.close();
			byte[] bin = swapStream.toByteArray();
			return bin;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	return null;
	}
	//base64
	public String getScreenshotBase64(JadbDevice device) {
		try {
			String cmd = "adb exec-out \"screencap -p | base64\"";
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream inputStream = process.getInputStream();
			return inputStreamToString(inputStream);
		} catch(IOException e) {
			log.info(e.getMessage());
		}
		return null;
	}
//	public void getScreenshotBase64(JadbDevice device, SimpMessagingTemplate messagingTemplate) {
//		try {
//			String cmd = "adb exec-out \"screencap -p | base64\"";
//			Process process = Runtime.getRuntime().exec(cmd);
//			new Thread(new ProcessHandleRunnable(process, messagingTemplate)).start();
//			new Thread(new ProcessHandleRunnable(process, null)).start();
//			process.waitFor();
//		} catch(IOException | InterruptedException e) {
//			log.info(e.getMessage());
//		}
//	}
	
	//获取当前显示的activity
	public String getFocusedActivity(JadbDevice device) {
		String shell = "dumpsys window | grep mFocusedWindow";
		String a = this.exeShell(device, shell);
		String[] b = a.substring(0, a.length() - 1).split(" ");
		return b[b.length - 1];
	}
	
	//点击操作
	public void click(JadbDevice device, int x, int y) {
		
		String shell = String.format("input tap %d %d", x, y);
		this.exeShell(device, shell);
	}
	
	//双击操作
	public void doubleClick(JadbDevice device, int x, int y) {
		
		String shell = String.format("input tap %d %d", x, y);
		try {
			device.executeShell(shell);
			Thread.sleep(100);
			device.executeShell(shell);
			Thread.sleep(1000);
		} catch (IOException | JadbException | InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	//滑屏操作
	public void swipe(JadbDevice device, Coord start, Coord end, int duration) {
		
		String shell = String.format("input swipe %d %d %d %d %d", 
				start.getX(), start.getY(), end.getX(), end.getY(), duration);
		this.exeShell(device, shell);
	}
	
	//返回操作
	public void back(JadbDevice device) {
		String shell = "input keyevent 4";
		this.exeShell(device, shell);
	}
	
	//设置输入法
	public void setIme(JadbDevice device, String name) {
		
		String shell = "ime list -a";
		String msg = this.exeShell(device, shell);
		Assert.isTrue(msg.indexOf(name) > -1, name + "输入法未安装，安装后使用");
		
		shell = String.format("ime set %s", name);
		msg = this.exeShell(device, shell);
		Assert.isTrue(msg.indexOf("selected") > -1, name + "输入法设置失败，手动设置后使用");
	}
	
	//打开app
	public void openActivity(JadbDevice device, String activity) {
		
		String shell = String.format("am start -n %s", activity);
		this.exeShell(device, shell);
	}
	
	//获取页面元素布局文件
	public void dumpUiFile(JadbDevice device) {
		String shell = "uiautomator dump --compressed /data/local/tmp/uidump.xml";
		int i = 0;
		while(i < 100) {
			String msg = this.exeShell(device, shell);
			log.info("→   获取ui响应{}：{}", i+1, msg);
			
			if(msg.indexOf("UI hierchary dumped to:") > -1) 
				return;
			else if(msg.indexOf("ERROR: could not get idle state") > -1) {
				Assert.isTrue(false, "could not get idle state");
			}
			
			//防止息屏 224：点亮屏幕
			this.keyEvent(device, 224);
			i++;
		}
		Assert.isTrue(i < 100, "获取布局文件失败");
	}
	
	//拉取页面元素布局
	public void saveUiFile(JadbDevice device, String path) {
		
		this.dumpUiFile(device);
		try {
			
			RemoteFile remoteFile = new RemoteFile("/data/local/tmp/uidump.xml");
			File file = new File(path);
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
	
	//判断当前activity是否是期望的activity
	public boolean isTargetActivity(JadbDevice device, String targetActivity) {
		
		boolean flag = false;
		String activity = getFocusedActivity(device);
		if(activity.indexOf(targetActivity) > -1) {
			flag = true;
		}
		return flag;
	}
	
	//输入消息
	public void sendMessage(JadbDevice device, String message) {
		String shell = String.format("am broadcast -a ADB_INPUT_TEXT --es msg %s", message);
		this.exeShell(device, shell);
	}

	public void sendMessage(JadbDevice device, String message, boolean bool) {
		String shell = String.format("adb shell am broadcast -a ADB_INPUT_TEXT --es msg %s", message);
		try {
			Runtime.getRuntime().exec(shell);
			Thread.sleep(1000);
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//keyevent
	public void keyEvent(JadbDevice device, int num) {
		//224 亮屏
		String shell = String.format("input keyevent %d", num);
		this.exeShell(device, shell);
	}
	
	//命令执行后需返回结果
	public String exeShell(JadbDevice device, String shell) {
		String msg = "";
		try {
			InputStream inputStream = device.executeShell(shell);
			Thread.sleep(afterActionWaitMillisecond);
			msg = this.inputStreamToString(inputStream);
		} catch (IOException | JadbException | InterruptedException e) {
			e.printStackTrace();
		}
		return msg.trim();
	}
	
	//InputStream to String
	public String inputStreamToString(InputStream inputStream){
		
		String str = null;
		try {
			str = new String(ByteStreams.toByteArray(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	public void setAfterActionWaitMillisecond(int millisecond) {
		afterActionWaitMillisecond = millisecond < defaultAfterActionWaitMillisecond ? defaultAfterActionWaitMillisecond : millisecond;
	}
}
