package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBikeRequest(
        @NotBlank(message = "{bike.create.name.blank}")
        String name,

        boolean isPublic,

        @NotNull(message = "{bike.create.type.required}")
        String type,

        @Size(max = 100, message = "{bike.create.brand.max}")
        String brand,

        @Size(max = 100, message = "{bike.create.model.max}")
        String model,

        @Min(value = 1900, message = "{bike.create.year.invalid}")
        Integer year,

        @Size(max = 100, message = "{bike.create.serial.max}")
        String serialNumber,

        @Size(max = 1000, message = "{bike.create.notes.max}")
        String notes,

        @Min(value = 0, message = "{bike.create.odometer.invalid}")
        Integer odometerKm,

        @Min(value = 0, message = "{bike.create.usage.invalid}")
        Integer usageTimeMinutes,

        boolean isExternalSync
) {
}
