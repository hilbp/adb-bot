package com.hilbp.adb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CameraHandlerTest {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        int userPoint = 0;
        int a = 10;
        int b = 10;
        int c = -userPoint;
        int level = (-b + new Double(Math.sqrt(b * b - 4 * a * c)).intValue()) / (2 * a);
        log.info("level: {}", level);
        int nextLevel = level + 1;
        int nextLevelPoint = 10 * nextLevel * (nextLevel + 1);
        int needPoint = nextLevelPoint - userPoint;
        log.info("needPoint: {}", needPoint);
        log.info("time: {}", System.currentTimeMillis() - start);
//        CameraHandler CameraHandler = new CameraHandler();
//        Thread thread = new Thread(CameraHandler);
//        thread.start();
    }
}