package com.quetoquenana.pedalpal.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_KEY = "bad.request";

    private final String messageKey;
    private final Object[] messageArgs;

    public BadRequestException() {
        this(DEFAULT_MESSAGE_KEY, (Object) null);
    }

    public BadRequestException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}

