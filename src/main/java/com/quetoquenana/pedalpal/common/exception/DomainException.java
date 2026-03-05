package com.quetoquenana.pedalpal.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private static final String DEFAULT_MESSAGE_KEY = "domain.exception";

    private final String messageKey;
    private final Object[] messageArgs;

    public DomainException() {
        this(DEFAULT_MESSAGE_KEY, (Object) null);
    }

    public DomainException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}

