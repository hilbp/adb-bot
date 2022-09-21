package com.hilbp.adb;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;

@Slf4j
public class AdbShellUtilTest {

    @Test
    public void test() {
        System.out.println(23423);
        this.run1();
    }

    public void run1() {
//        String cmd = "adb exec-out \"while true; do screenrecord --bit-rate=16m --output-format=h264 --time-limit 180 -; done\"";
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 10 -";
        String result="";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream(), Charset.forName("UTF-8")));
            String line = null;
            int i = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                this.createImg(i, line);
                i++;
            }

            br.close();
            System.out.println("close ... ");
            ps.waitFor();
            System.out.println("wait over ...");


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void createImg(int i, String data) {

        File file =new File("E://adb/test" + i + ".png");

        //if file doesnt exists, then create it
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //true = append file
        FileWriter fileWritter = null;
        try {
            fileWritter = new FileWriter(file.getName(),true);
            fileWritter.write(data);
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Done");
    }

    public void run2() {
//        String cmd = "adb exec-out screenrecord --bit-rate=16m --size 1920x1080 -";
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 180 -";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();

            log.info(inputStreamToString(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
//        String cmd = "adb exec-out screenrecord --bit-rate=16m --size 1920x1080 -";
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 10 -";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);

            frameGrabber.setFrameRate(30);
            frameGrabber.setFormat("h264");
            frameGrabber.setVideoBitrate(15);
            frameGrabber.setVideoOption("preset", "ultrafast");
            frameGrabber.setNumBuffers(25000000);
            log.info("start: 12");
            frameGrabber.start();
            log.info("end:12");

            Frame frame = frameGrabber.grab();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            int i = 0;
            while (frame != null) {
                log.info("i: {}", i);
                Buffer[] bf = frame.image;

                BufferedImage bufferedImage = converter.convert(frame);
                File outputfile = new File("E://adb/image"+ i +".png");
                ImageIO.write(bufferedImage, "png", outputfile);
                i++;
                frame = frameGrabber.grab();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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
}