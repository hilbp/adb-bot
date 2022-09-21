package com.hilbp.adb.util;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CameraHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CameraHandler.class);

    private static BufferedImage bufferedImage;

    @Override
    public void run() {
        try {
            String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 10 -";
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();

            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);

            frameGrabber.setFrameRate(30);
            frameGrabber.setFormat("h264");
            frameGrabber.setVideoBitrate(15);
            frameGrabber.setVideoOption("preset", "ultrafast");
            frameGrabber.setNumBuffers(25000000);

            frameGrabber.start();

            Frame frame = frameGrabber.grab();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            int i = 0;
            while (frame != null) {
                log.info("i: {}", i);
                BufferedImage bufferedImage = converter.convert(frame);
                File outputfile = new File("E://adb/image"+ i +".png");
                ImageIO.write(bufferedImage, "png", outputfile);
                i++;
                frame = frameGrabber.grab();
            }
        } catch (IOException e) {
            logger.info("Video handle error, exit ...");
            logger.info(e.getMessage());
        }
    }

}