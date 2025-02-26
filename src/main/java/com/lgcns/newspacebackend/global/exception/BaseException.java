package com.lgcns.newspacebackend.global.exception;

public class BaseException extends RuntimeException {

    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}
