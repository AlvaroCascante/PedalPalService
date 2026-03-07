package com.quetoquenana.pedalpal.serviceorder.application.result;

import com.quetoquenana.pedalpal.common.domain.model.UserType;

import java.time.Instant;
import java.util.UUID;

/*
Application result used when returning comments for a ServiceOrder.
*/
public record ServiceOrderCommentResult(
        UUID id,
        UUID serviceOrderId,
        String comment,
        Boolean customerVisible,
        UserType createdByType,
        Instant createdAt,
        UUID createdBy
) {
}