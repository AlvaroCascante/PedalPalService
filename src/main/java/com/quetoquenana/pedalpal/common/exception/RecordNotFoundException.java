package com.quetoquenana.pedalpal.common.exception;

import lombok.Getter;

@Getter
public class RecordNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE_KEY = "record.not.found";

    private final String messageKey;
    private final Object[] messageArgs;

    public RecordNotFoundException() {
        this(DEFAULT_MESSAGE_KEY, (Object) null);
    }

    public RecordNotFoundException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

}
