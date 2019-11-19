package com.selfimpr.base.uniformDataReturn.codeenum;

/**
 * 自定义用户相关统一返回枚举
 */
public enum UserResultCode implements CodeEnums {

    NODATA(10, "查询成功无记录"),
    ACCOUNT_ERROR(11, "账户不存在或被禁用");

    private Integer code;
    private String codeDesc;

    UserResultCode(Integer code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getCodeDesc() {
        return codeDesc;
    }
}
