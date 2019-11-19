package com.selfimpr.base.uniformException;

import com.selfimpr.base.uniformDataReturn.codeenum.CodeEnums;

/**
 * 自定义异常
 * <p>
 * 注意要继承自RuntimeException而不是Exception，继承自Exception的话，当抛出自定义异常时spring事务不会回滚
 */
public class CustomException extends RuntimeException {

    /**
     * 自定义增加状态枚举
     */
    private CodeEnums codeEnums;

    public CustomException(CodeEnums codeEnums, String message) {
        // 把自定义的message传递个异常父类
        super(message);
        this.codeEnums = codeEnums;
    }

    public CodeEnums getCodeEnums() {
        return codeEnums;
    }

    public void setCodeEnums(CodeEnums codeEnums) {
        this.codeEnums = codeEnums;
    }
}
