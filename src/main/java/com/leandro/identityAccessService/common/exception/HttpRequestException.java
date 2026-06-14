package com.leandro.identityAccessService.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class HttpRequestException extends RuntimeException {

    private final HttpStatusCode statusCode;
    private final String responseBody;

    public HttpRequestException(HttpStatusCode statusCode, String message, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpRequestException(HttpStatusCode statusCode, String message) {
        this(statusCode, message, null);
    }

    public boolean notFound(){
        return statusCode.equals(HttpStatus.NOT_FOUND);
    }
}
