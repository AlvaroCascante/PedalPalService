package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBikeRequest(
        @NotBlank(message = "{bike.name.blank}")
        @Size(max = 50, message = "{bike.name.max}")
        String name,

        @NotBlank(message = "{bike.type.required}")
        String type,

        @Size(max = 50, message = "{bike.brand.max}")
        String brand,

        @Size(max = 50, message = "{bike.model.max}")
        String model,

        @Min(value = 1900, message = "{bike.year.invalid}")
        Integer year,

        @Size(max = 50, message = "{bike.serial.max}")
        String serialNumber,

        @Size(max = 250, message = "{bike.notes.max}")
        String notes,

        @Min(value = 0, message = "{bike.odometer.invalid}")
        Integer odometerKm,

        @Min(value = 0, message = "{bike.usage.invalid}")
        Integer usageTimeMinutes,

        @Size(max = 100, message = "{bike.externalGearId.max}")
        String externalGearId,

        @Size(max = 50, message = "{bike.externalSyncProvider.max}")
        String externalSyncProvider,

        boolean isPublic,

        boolean isExternalSync
) {
}
