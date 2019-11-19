package com.selfimpr.base.uniformDataReturn.codeenum;

/**
 * 自定义用户相关统一返回枚举
 */
public enum UserResultCode implements CodeEnums {

    NODATA(10,"查询成功无记录"),
    ACCOUNT_ERROR(11, "账户不存在或被禁用");

    private int code;
    private String desc;

    UserResultCode(int code, String desc) {
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
