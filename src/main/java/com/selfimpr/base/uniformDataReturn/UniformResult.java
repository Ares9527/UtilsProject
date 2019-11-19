package com.selfimpr.base.uniformDataReturn;

import com.selfimpr.base.uniformDataReturn.codeenum.BaseResultCode;
import com.selfimpr.base.uniformDataReturn.codeenum.CodeEnums;
import com.selfimpr.base.uniformDataReturn.codeenum.UserResultCode;

import java.io.Serializable;

/**
 * 自定义接口统一结果返回
 */
public class UniformResult<T> implements Serializable {

    private static final long serialVersionUID = -5462019593165698036L;

    /**
     * 头部信息
     */
    private HeadData head;

    /**
     * 返回数据
     */
    private T data;

    public UniformResult() {
    }

    public UniformResult(HeadData head, T data) {
        this.head = head;
        this.data = data;
    }

    public HeadData getHead() {
        return head;
    }

    public void setHead(HeadData head) {
        this.head = head;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // TODO 基础
    /**
     * 成功默认返回
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> UniformResult<T> success(T data) {
        return constructResult(BaseResultCode.SUCCESS, data);
    }

    /**
     * 成功默认返回
     *
     * @param <T>
     * @return
     */
    public static <T> UniformResult<T> failed() {
        return constructResult(BaseResultCode.FEAILED, null);
    }

    // TODO 例：用户相关

    /**
     * 查询用户成功但不存在
     *
     * @param <T>
     * @return
     */
    public static <T> UniformResult<T> userQuerySuccessNoData() {
        return constructResult(UserResultCode.NODATA, null);
    }

    /**
     * 构造统一结果返回
     *
     * @param codeEnums
     * @param data
     * @param <T>
     * @return
     */
    private static <T> UniformResult<T> constructResult(CodeEnums codeEnums, T data) {
        UniformResult<T> result = new UniformResult<>();
        result.setHead(new HeadData(codeEnums.getCode(), codeEnums.getDesc()));
        result.setData(data);
        return result;
    }
}
