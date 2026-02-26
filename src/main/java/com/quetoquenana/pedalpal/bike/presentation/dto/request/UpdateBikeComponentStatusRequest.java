package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * PATCH request DTO for bike component status updates.
 */
public record UpdateBikeComponentStatusRequest(
        @NotNull(message = "{bike.component.update.status.blank}")
        String status
) {
}
