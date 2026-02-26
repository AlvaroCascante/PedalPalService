package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * PATCH request DTO for partial bike updates.
 * Semantics:
 * - Missing field => no change
 * - Present field => validate + apply
 * - Explicit null => rejected (400)
 */
public record UpdateBikeRequest(
        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.name.blank}")
        String name,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.brand.blank}")
        String brand,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.model.blank}")
        String model,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 1900, message = "{bike.update.year.invalid}")
        Integer year,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.type.blank}")
        String type,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.serial.blank}")
        String serialNumber,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.update.notes.blank}")
        String notes,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.update.odometer.invalid}")
        Integer odometerKm,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.update.usage.invalid}")
        Integer usageTimeMinutes,

        @JsonSetter(contentNulls = Nulls.FAIL)
        Boolean isPublic,

        @JsonSetter(contentNulls = Nulls.FAIL)
        Boolean isExternalSync
) {
}
