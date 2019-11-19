package com.selfimpr.base.uniformDataReturn;

import java.io.Serializable;

/**
 * 自定义头部信息
 */
public class HeadData implements Serializable {

    private static final long serialVersionUID = -6837135410620848131L;

    /**
     * 状态码
     */
    private int code;
    /**
     * 状态码描述
     */
    private String desc;
    /**
     * 时间
     */
    private long timeStamp = System.currentTimeMillis();

    public HeadData() {
    }

    public HeadData(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "HeadData{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
