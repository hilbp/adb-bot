package com.hilbp.adb;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;

@Slf4j
public class ScreenRecordTest {

    @Test
    public void test() {
        this.run2();
    }
    public void run3() {
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 15 -";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = ps.getInputStream();
            this.inputStreamFfmpeg(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run2() {
        String path = "E://adb/lbp1.mp4";
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 15 -";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = ps.getInputStream();
            this.inputStreamToFile(inputStream, path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileFfmpeg(path);
    }
    public void run1() {
        String cmd = "adb exec-out screenrecord --bit-rate=16m --output-format=h264 --time-limit 15 -";
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = ps.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {

                System.out.println(line);
                this.appendFile(line);

            }
            ps.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        this.fileFfmpeg("E://adb/lbp.mp4");
    }

    public void inputStreamFfmpeg(InputStream inputStream) {
        log.info("222");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("23423");
                if(inputStream == null) {
                    log.info("null");
                }
                FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
                FfmpegUtil(frameGrabber);
            }
        });
        thread.start();


    }

    public void fileFfmpeg(String path) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(path);
        this.FfmpegUtil(frameGrabber);
    }
    public void FfmpegUtil(FFmpegFrameGrabber frameGrabber) {

        try {
            log.info("start");
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
    public void appendFile(String data) {
        File file =new File("E://adb/lbp2.mp4");

        try {
            //if file doesnt exists, then create it
            if(!file.exists()){
                file.createNewFile();
            }
            //true = append file
            FileWriter fileWritter = new FileWriter(file.getName(),true);
            fileWritter.write(data);
            fileWritter.close();
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inputStreamToFile(InputStream is, String fileName) {
        OutputStream outputStream=null;
        File file = new File(fileName);
        try {
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            int len=-1;
            byte[] b = new byte[1024];
            int i = 0;
            while((len = in.read(b)) != -1){
                out.write(b,0,len);
                i++;
                if(i > 3)
                    break;
            }
//            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}