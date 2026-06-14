package com.leandro.identityAccessService.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PERMISSION_NOT_FOUND("PERMISSION_NOT_FOUND", "Permission not found"),
    ROLE_ALREADY_EXISTS("ROLE_ALREADY_EXISTS", "Role already exists"),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found"),
    PERMISSION_ALREADY_EXISTS("PERMISSION_ALREADY_EXISTS", "Permission already exists"),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists"),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),

    VALIDATION_ERROR("VALIDATION_ERROR", "Request validation failed"),
    INTERNAL_ERROR("INTERNAL_ERROR", "An unexpected error occurred. Please contact support."),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized");

    private final String code;
    private final String message;
}

