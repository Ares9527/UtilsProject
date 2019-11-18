package com.selfimpr.entityAndDto;

import com.selfimpr.excel.ExcelColumn;

public class UserDTO {

    @ExcelColumn(columnName = "codeid", order = 1)
    private String codeid;

    @ExcelColumn(columnName = "userid", order = 1)
    private String userid;

    public String getCodeid() {
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "codeid='" + codeid + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
