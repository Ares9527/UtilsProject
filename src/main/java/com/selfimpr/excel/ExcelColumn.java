package com.selfimpr.excel;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    // 列名
    String columnName() default "";

    // 顺序
    int order() default 0;
}
