package com.hilbp.sample.test;//package com.hilbp.adb.test;
//
//import com.hilbp.adb.util.StaticValue;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.support.GenericMessage;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//@Component
//@Slf4j
//public class RecorderScreen {
//
////    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void test() {
//        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 180 -";
//        try {
//            Process ps = Runtime.getRuntime().exec(cmd);
//            InputStream inputStream = ps.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String line = null;
//            while ((line = bufferedReader.readLine()) != null) {
//
//                System.out.println(line);
//                byte [] byteArray = line.getBytes();
//                Message<byte[]> data = new GenericMessage(byteArray);
//
//                messagingTemplate.send(StaticValue.SOCKET_SCREEN_VIDEO, data);
//            }
//            ps.waitFor();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
