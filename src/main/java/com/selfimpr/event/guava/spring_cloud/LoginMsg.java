package com.selfimpr.event.guava.spring_cloud;

public class LoginMsg {

    private Long uid;
    private String mobile;
    private String ip;
    private String osVersion;
    private String deviceModel;
    private String deviceToken;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "LoginMsg{" +
                "uid=" + uid +
                ", mobile='" + mobile + '\'' +
                ", ip='" + ip + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
