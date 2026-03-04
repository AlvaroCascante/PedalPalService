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
        @Size(min = 1, message = "{bike.name.blank}")
        @Size(max = 50, message = "{bike.name.max}")
        String name,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.brand.blank}")
        @Size(max = 50, message = "{bike.brand.max}")
        String brand,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.model.blank}")
        @Size(max = 50, message = "{bike.model.max}")
        String model,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 1900, message = "{bike.year.invalid}")
        Integer year,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.type.blank}")
        String type,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.serial.blank}")
        String serialNumber,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.notes.blank}")
        String notes,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.odometer.invalid}")
        Integer odometerKm,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.usage.invalid}")
        Integer usageTimeMinutes,

        @JsonSetter(contentNulls = Nulls.FAIL)
        Boolean isPublic,

        @JsonSetter(contentNulls = Nulls.FAIL)
        Boolean isExternalSync
) {
}
