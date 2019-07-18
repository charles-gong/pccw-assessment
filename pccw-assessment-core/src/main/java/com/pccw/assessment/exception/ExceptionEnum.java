package com.pccw.assessment.exception;

public enum ExceptionEnum {

    UNKONW_ERROR(-1, "UNKNOW_ERROR"),

    SUCCESS(200, "SUCCESS"),

    NOT_FOUND(404, "NOT_FOUND"),

    EXIST(401, "EXIST");

    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
