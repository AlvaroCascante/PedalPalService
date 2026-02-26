package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddBikeComponentRequest(
        @NotNull(message = "{bike.add.component.name.blank}")
        String name,

        @NotNull(message = "{bike.add.component.type.required}")
        String type,

        @Size(max = 100, message = "{bike.add.component.brand.max}")
        String brand,

        @Size(max = 100, message = "{bike.add.component.model.max}")
        String model,

        @Size(max = 1000, message = "{bike.add.component.notes.max}")
        String notes,

        @Min(value = 0, message = "{bike.add.component.odometer.invalid}")
        int odometerKm,

        @Min(value = 0, message = "{bike.add.component.usage.invalid}")
        int usageTimeMinutes
) {
}
