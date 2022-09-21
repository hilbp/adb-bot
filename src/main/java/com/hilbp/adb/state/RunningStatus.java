package com.hilbp.adb.state;

import lombok.Data;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RunningStatus {

    @Data
    private static class Status {
        boolean finished = false; //是否完成
        boolean abortSubsteps = false; //是否中断子流程
    }

    private static Map<String, Status> statusSet = new ConcurrentHashMap<>();

    public static void setFinished(JadbDevice device) {

        Status status = new Status();
        status.setFinished(true);
        status.setAbortSubsteps(true);
        statusSet.put(device.getSerial(), status);
    }

    public static boolean isFinished(JadbDevice device) {

        boolean flag = false;
        Status status = statusSet.getOrDefault(device.getSerial(), null);
        if(status != null)
            flag = status.finished;
        return flag;
    }

    public static void setAbortSubsteps(JadbDevice device) {

        Status status = new Status();
        status.setFinished(false);
        status.setAbortSubsteps(true);
        statusSet.put(device.getSerial(), status);
    }

    public static void clearAbortSubsteps(JadbDevice device) {

        Status status = statusSet.get(device.getSerial());
        status.setAbortSubsteps(false);
        statusSet.put(device.getSerial(), status);
    }

    public static boolean isAbortSubsteps(JadbDevice device) {

        boolean flag = false;
        Status status = statusSet.getOrDefault(device.getSerial(), null);
        if(status != null)
            flag = status.abortSubsteps;
        return flag;
    }






}
