package com.quetoquenana.pedalpal.presentation.dto.api.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PATCH request DTO for bike status updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBikeStatusRequest {

    @NotNull(message = "{bike.update.status.required}")
    private String status;
}
