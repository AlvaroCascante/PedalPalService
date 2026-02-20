package com.quetoquenana.pedalpal.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBikeComponentRequest {

    @NotNull(message = "{bike.add.component.name.blank}")
    private String name;

    @NotNull(message = "{bike.add.component.type.required}")
    private String type;

    @Size(max = 100, message = "{bike.add.component.brand.max}")
    private String brand;

    @Size(max = 100, message = "{bike.add.component.model.max}")
    private String model;

    @Size(max = 1000, message = "{bike.add.component.notes.max}")
    private String notes;

    @Min(value = 0, message = "{bike.add.component.odometer.invalid}")
    private int odometerKm;

    @Min(value = 0, message = "{bike.add.component.usage.invalid}")
    private int usageTimeMinutes;
}
