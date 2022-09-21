package com.hilbp.adb.action;

import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.DeviceInfo;
import com.hilbp.adb.util.AdbShellUtil;
import com.hilbp.adb.util.StringUtil;
import com.hilbp.adb.yml.ActionDataBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import se.vidstige.jadb.JadbDevice;

import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class RunAdbBot {

    @Value("${adbConfig.adbBotActionSchedule}")
    private String adbBotActionSchedule;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConfigurableEnvironment environment;

    @Autowired
    AdbShellUtil adbShellUtil;

    @Autowired
    ActionDataBinder actionDataBinder;

    public void start(JadbDevice device, String yml) {

        ActionSchedule actionSchedule = (ActionSchedule) applicationContext.getBean(adbBotActionSchedule);
        Assert.notNull(actionSchedule, "调度器未配置");

        init(device);
        List<Action> actions = actionDataBinder.actionBinderYml(yml).getActions();
        actionSchedule.run(device, actions);
    }

    public JadbDevice getDeviceByIndex(int index) {

        JadbDevice device = adbShellUtil.getDevices().get(index);
        return device;
    }

    //进行初始化，设置当前正在执行的设备信息
    private void init(JadbDevice device) {

        Binder binder = Binder.get(environment);
        String[] info = device.getSerial().split(":");
        String prefix = String.format("devicesConfig.%s", info[0]).toLowerCase();
        DeviceInfo deviceInfo = binder.bind(prefix, Bindable.of(DeviceInfo.class)).get();

        Properties properties = new Properties();
        if(StringUtil.isNotEmpty(deviceInfo.getHomeActivity()))
            properties.setProperty("devicesConfig.default.homeActivity", deviceInfo.getHomeActivity());
        if(StringUtil.isNotEmpty(deviceInfo.getImeDefaultKeyboard()))
            properties.setProperty("devicesConfig.default.imeDefaultKeyboard", deviceInfo.getImeDefaultKeyboard());
        if(StringUtil.isNotEmpty(deviceInfo.getImeAdbKeyboard()))
            properties.setProperty("devicesConfig.default.imeAdbKeyboard", deviceInfo.getImeAdbKeyboard());
        if(deviceInfo.getHeight() != null)
            properties.setProperty("devicesConfig.default.height", deviceInfo.getHeight().toString());
        if(deviceInfo.getWidth() != null)
            properties.setProperty("devicesConfig.default.width", deviceInfo.getWidth().toString());

        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("currentDeviceInfo",properties);
        environment.getPropertySources().addFirst(propertiesPropertySource);
        applicationContext.publishEvent(environment);

    }
}
