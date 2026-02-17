package com.quetoquenana.pedalpal.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_KEY = "business.exception";

    private final String messageKey;
    private final Object[] messageArgs;

    public BusinessException() {
        this(DEFAULT_MESSAGE_KEY, (Object) null);
    }

    public BusinessException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}

