package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddBikeComponentRequest(
        @NotBlank(message = "{bike.component.name.blank}")
        @Size(max = 50, message = "{bike.component.name.max}")
        String name,

        @NotBlank(message = "{bike.component.type.required}")
        String type,

        @Size(max = 50, message = "{bike.component.brand.max}")
        String brand,

        @Size(max = 50, message = "{bike.component.model.max}")
        String model,

        @Size(max = 250, message = "{bike.component.notes.max}")
        String notes,

        @Min(value = 0, message = "{bike.component.odometer.invalid}")
        int odometerKm,

        @Min(value = 0, message = "{bike.component.usage.invalid}")
        int usageTimeMinutes
) {
}
