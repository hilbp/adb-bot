package com.hilbp.sample.test;//package com.hilbp.adb.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Component
//@Slf4j
//public class ScreenRecorder {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void test() {
//        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 180 -";
//        try {
//            Process ps = Runtime.getRuntime().exec(cmd);
//            InputStream inputStream = ps.getInputStream();
//
//            FfmpegH264 ffmpegH264 = new FfmpegH264(inputStream, messagingTemplate);
//            log.info("new Thread(ffmpegH264).start()");
//            new Thread(ffmpegH264).start();
//            log.info("new Thread(ffmpegH264).start() end");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
