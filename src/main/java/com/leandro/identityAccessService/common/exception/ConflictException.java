package com.leandro.identityAccessService.common.exception;

public class ConflictException extends ApiException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConflictException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
