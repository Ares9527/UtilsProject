package com.selfimpr.entityAndDto;

import com.selfimpr.excel.ExcelColumn;

public class People {

    @ExcelColumn(columnName = "name", order = 1)
    private String name;

    @ExcelColumn(columnName = "age", order = 2)
    private int age;

    public People() {}

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}