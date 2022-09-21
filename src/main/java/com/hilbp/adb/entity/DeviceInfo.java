package com.hilbp.adb.entity;

import lombok.Data;

@Data
public class DeviceInfo {

    private String homeActivity; //手机主界面
    private String imeDefaultKeyboard; //默认输入法
    private String imeAdbKeyboard; //adb keyborad输入法
    private Integer height; //高，单位像素
    private Integer width; //宽，单位像素


}
