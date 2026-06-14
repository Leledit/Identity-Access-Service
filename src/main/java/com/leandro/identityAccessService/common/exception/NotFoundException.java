package com.leandro.identityAccessService.common.exception;

public class NotFoundException extends ApiException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
