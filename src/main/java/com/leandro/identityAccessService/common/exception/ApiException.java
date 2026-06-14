package com.leandro.identityAccessService.common.exception;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected ApiException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}