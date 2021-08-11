package com.zyf.model;

/**
 * 当前设备相关信息
 */
public class DeviceInfo {
    public String uuid;//设备唯一码
    public String isTablet; //手机还是平板
    public String Devmodel; //设备型号

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIsTablet() {
        return isTablet;
    }

    public void setIsTablet(String isTablet) {
        this.isTablet = isTablet;
    }

    public String getDevmodel() {
        return Devmodel;
    }

    public void setDevmodel(String devmodel) {
        Devmodel = devmodel;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "uuid='" + uuid + '\'' +
                ", isTablet='" + isTablet + '\'' +
                ", Devmodel='" + Devmodel + '\'' +
                '}';
    }
}
