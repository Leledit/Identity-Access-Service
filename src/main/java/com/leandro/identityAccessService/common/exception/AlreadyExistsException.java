package com.leandro.identityAccessService.common.exception;

public class AlreadyExistsException extends ApiException {

    public AlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyExistsException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
