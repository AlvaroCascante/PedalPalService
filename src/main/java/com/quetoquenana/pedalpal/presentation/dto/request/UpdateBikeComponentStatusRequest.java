package com.quetoquenana.pedalpal.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PATCH request DTO for bike component status updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBikeComponentStatusRequest {

    @NotNull(message = "{bike.component.update.status.blank}")
    private String status;
}
