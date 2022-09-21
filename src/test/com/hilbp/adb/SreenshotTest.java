package com.hilbp.adb;

import com.alibaba.fastjson.JSON;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SreenshotTest {

    @Test
    public void test() {
        for(int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            try {
                String cmd = "adb exec-out \"screencap -p | base64\"";
                Process process = Runtime.getRuntime().exec(cmd);
                InputStream inputStream = process.getInputStream();
                String str = inputStreamToString(inputStream);
//                log.info(inputStreamToString(inputStream));
            } catch(IOException e) {
                log.info(e.getMessage());
            }
//            this.run();
            log.info("time: {}", System.currentTimeMillis() - start);
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

    public void run(){
        String cmd = "adb exec-out screencap -p";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = process.getInputStream();

            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[2048];
            int rc = 0;

            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] bin = swapStream.toByteArray();
            String jsonStr = JSON.toJSONString(bin);
            log.info(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}