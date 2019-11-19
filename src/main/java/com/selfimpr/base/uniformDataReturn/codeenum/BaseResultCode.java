package com.selfimpr.base.uniformDataReturn.codeenum;

/**
 * 自定义基本默认返回枚举
 */
public enum BaseResultCode implements CodeEnums {

    SUCCESS(0, "成功"),
    FEAILED(1, "失败");
    // 待扩展

    private int code;
    private String desc;

    BaseResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
