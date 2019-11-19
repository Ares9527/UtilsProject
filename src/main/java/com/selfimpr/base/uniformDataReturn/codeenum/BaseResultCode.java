package com.selfimpr.base.uniformDataReturn.codeenum;

/**
 * 自定义基本默认返回枚举
 */
public enum BaseResultCode implements CodeEnums {

    SUCCESS(0, "成功"),
    FEAILED(1, "失败"),
    PARAMETERS_ERROR(2, "参数错误");
    // 待扩展

    private Integer code;
    private String codeDesc;

    BaseResultCode(Integer code, String codeDesc) {
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
