package com.quetoquenana.pedalpal.exception;

import lombok.Getter;

@Getter
public class ForbiddenAccessException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_KEY = "authentication.required";

    private final String messageKey;
    private final Object[] messageArgs;

    public ForbiddenAccessException() {
        this(DEFAULT_MESSAGE_KEY, (Object) null);
    }

    public ForbiddenAccessException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}
