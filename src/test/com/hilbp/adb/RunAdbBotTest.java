package com.hilbp.adb;

import com.hilbp.adb.action.RunAdbBot;
import com.hilbp.adb.util.AdbShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import se.vidstige.jadb.JadbDevice;

import java.io.IOException;
import java.util.Properties;


@SpringBootTest
@Slf4j
class RunAdbBotTest {

    @Autowired
    RunAdbBot runAdbBot;

    @Autowired
    AdbShellUtil adbShellUtil;

    @Test
    void start() {
        JadbDevice device = adbShellUtil.getDevices().get(0);
        runAdbBot.start(device, "wechat-public-account-search");
    }

    @Test
    void start1() {

        JadbDevice device = runAdbBot.getDeviceByIndex(0);
        String now = adbShellUtil.getFocusedActivity(device);
        log.info("device: {}", device.getSerial());
        log.info("getFocusedActivity: {}", now);

//        adbShellUtil.keyEvent(device, 66);
    }

    @Autowired
    ConfigurableEnvironment environment;


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    //https://blog.csdn.net/zhang_jingrui/article/details/122348584
    @Test
    void ymlTest() throws IOException {

        Properties properties = new Properties();
        properties.setProperty("task.enable", "false");
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("my",properties);
        configurableEnvironment.getPropertySources().addFirst(propertiesPropertySource);
        applicationContext.publishEvent(environment);

        log.info("res: {}", environment.getProperty("task.enable"));
        log.info("hhaha: {}", environment.getProperty("hhaha"));

        properties.setProperty("task.enable", "true");
        PropertiesPropertySource propertiesPropertySource1 = new PropertiesPropertySource("my",properties);
        configurableEnvironment.getPropertySources().addFirst(propertiesPropertySource1);
        applicationContext.publishEvent(environment);

        log.info("res: {}", environment.getProperty("task.enable"));
        log.info("hhaha: {}", environment.getProperty("hhaha"));




    }
}