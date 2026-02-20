package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBikeRequest {

    @NotBlank(message = "{bike.create.name.blank}")
    private String name;

    private boolean isPublic;

    @NotNull(message = "{bike.create.type.required}")
    private String type;

    @Size(max = 100, message = "{bike.create.brand.max}")
    private String brand;

    @Size(max = 100, message = "{bike.create.model.max}")
    private String model;

    @Min(value = 1900, message = "{bike.create.year.invalid}")
    private Integer year;

    @Size(max = 100, message = "{bike.create.serial.max}")
    private String serialNumber;

    @Size(max = 1000, message = "{bike.create.notes.max}")
    private String notes;

    @Min(value = 0, message = "{bike.create.odometer.invalid}")
    private int odometerKm;

    @Min(value = 0, message = "{bike.create.usage.invalid}")
    private int usageTimeMinutes;

    private boolean isExternalSync;
}
