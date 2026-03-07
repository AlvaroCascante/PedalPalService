package com.quetoquenana.pedalpal.serviceorder.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

/*
Response DTO for ServiceOrder comments.
*/
public record ServiceOrderCommentResponse(
        UUID id,
        UUID serviceOrderId,
        String comment,
        Boolean customerVisible,
        String createdByType,
        Instant createdAt,
        UUID createdBy
) {
}