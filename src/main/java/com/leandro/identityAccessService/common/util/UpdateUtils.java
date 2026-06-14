package com.leandro.identityAccessService.common.util;

import java.util.function.Consumer;

public class UpdateUtils {

    private UpdateUtils() {}

    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}

