package com.quetoquenana.pedalpal.serviceorder.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * API request to add a comment to a service order.
 */
public record AddServiceOrderCommentRequest(
        @NotBlank(message = "{service.order.comment.blank}")
        @Size(max = 1000, message = "{service.order.comment.max.length}")
        String comment,
        boolean customerVisible
) {
}

