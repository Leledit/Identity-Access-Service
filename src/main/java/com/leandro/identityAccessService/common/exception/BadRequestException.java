package com.leandro.identityAccessService.common.exception;

public class BadRequestException extends ApiException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
