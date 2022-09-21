package com.hilbp.sample.test;//package com.hilbp.adb.test;
//
//import com.alibaba.fastjson.JSON;
//import com.hilbp.adb.util.StaticValue;
//import lombok.extern.slf4j.Slf4j;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.Java2DFrameConverter;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Slf4j
//public class FfmpegH264 implements Runnable {
//    InputStream inputStream;
//    SimpMessagingTemplate messagingTemplate;
//
//    public FfmpegH264(InputStream inputStream, SimpMessagingTemplate messagingTemplate) {
//        this.inputStream = inputStream;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public void run() {
//        inputStreamFfmpeg(this.inputStream);
//    }
//
//    public void inputStreamFfmpeg(InputStream inputStream) {
//        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
//        FfmpegUtil(frameGrabber);
//    }
//
//    public void FfmpegUtil(FFmpegFrameGrabber frameGrabber) {
//        try {
//            log.info("start");
//            frameGrabber.setFrameRate(30);
//            frameGrabber.setFormat("h264");
//            frameGrabber.setVideoBitrate(15);
//            frameGrabber.setVideoOption("preset", "ultrafast");
//            frameGrabber.setNumBuffers(25000000);
//            log.info("start: frameGrabber.startUnsafe(false);");
//            frameGrabber.startUnsafe(false);
//            log.info("end: frameGrabber.startUnsafe(false);");
//
//            Frame frame = frameGrabber.grab();
//
//            Java2DFrameConverter converter = new Java2DFrameConverter();
//            int i = 0;
//            while (frame != null) {
//                log.info("i: {}", i);
//                BufferedImage bufferedImage = converter.convert(frame);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(bufferedImage, "png", baos);
//                byte [] byteArray = baos.toByteArray();
//				String jsonStr = JSON.toJSONString(byteArray);
//                messagingTemplate.convertAndSend(StaticValue.SOCKET_SCREEN_MIRROR, jsonStr);
////                Message<byte[]> data = new GenericMessage(byteArray);
////                messagingTemplate.send(StaticValue.SOCKET_SCREEN_MIRROR, data);
//
////                File outputfile = new File("E://adb/image"+ i +".png");
////                ImageIO.write(bufferedImage, "png", outputfile);
//                i++;
//                frame = frameGrabber.grab();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
