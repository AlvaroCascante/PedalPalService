package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * PATCH request DTO for partial bike component updates.
 * Semantics:
 * - Missing field => no change
 * - Present field => validate + apply
 * - Explicit null => rejected (400)
 */
public record UpdateBikeComponentRequest(
        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.component.name.blank}")
        String name,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, message = "{bike.component.type.required}")
        String type,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, max = 50, message = "{bike.component.brand.max}")
        String brand,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, max = 50, message = "{bike.component.model.max}")
        String model,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Size(min = 1, max = 250, message = "{bike.component.notes.max}")
        String notes,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.component.odometer.invalid}")
        Integer odometerKm,

        @JsonSetter(contentNulls = Nulls.FAIL)
        @Min(value = 0, message = "{bike.component.usage.invalid}")
        Integer usageTimeMinutes
) {
}
