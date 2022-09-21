package com.hilbp.sample.test;//package com.hilbp.adb.test;
//
//import com.google.common.io.ByteStreams;
//import com.hilbp.adb.util.StaticValue;
//import com.hilbp.adb.util.StringUtil;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class ProcessHandleRunnable implements Runnable {
//
//    private Process process;
//    private SimpMessagingTemplate messagingTemplate;
//
//    public ProcessHandleRunnable(Process process, SimpMessagingTemplate messagingTemplate) {
//        this.process = process;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public void run() {
//        if(this.messagingTemplate != null) {
//            String base64Str = this.inputStreamToString(this.process.getInputStream());
//            if(StringUtil.isNotEmpty(base64Str))
//                messagingTemplate.convertAndSend(StaticValue.SOCKET_SCREEN_MIRROR, base64Str);
//        } else {
//            this.inputStreamToString(this.process.getErrorStream());
//        }
//
//    }
//
//    //InputStream to String
//    public String inputStreamToString(InputStream inputStream){
//
//        String str = null;
//        try {
//            str = new String(ByteStreams.toByteArray(inputStream));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//}
