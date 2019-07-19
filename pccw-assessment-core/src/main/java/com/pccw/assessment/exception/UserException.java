package com.pccw.assessment.exception;

public class UserException extends RuntimeException {

    private Integer code;

    public UserException(ExceptionEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public UserException(int code, String message) {
        super(message);
        this.code = code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
