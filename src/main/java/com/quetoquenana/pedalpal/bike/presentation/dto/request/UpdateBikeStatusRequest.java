package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * PATCH request DTO for bike status updates.
 */
public record UpdateBikeStatusRequest(
        @NotNull(message = "{bike.update.status.blank}")
        String status
) {
}
