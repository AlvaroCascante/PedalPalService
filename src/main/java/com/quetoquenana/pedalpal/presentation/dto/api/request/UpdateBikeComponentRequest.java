package com.quetoquenana.pedalpal.presentation.dto.api.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBikeComponentRequest {

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.add.component.name.blank}")
    private String name;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.add.component.type.required}")
    private String type;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, max = 100, message = "{bike.add.component.brand.max}")
    private String brand;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, max = 100, message = "{bike.add.component.model.max}")
    private String model;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, max = 1000, message = "{bike.add.component.notes.max}")
    private String notes;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 0, message = "{bike.add.component.odometer.invalid}")
    private int odometerKm;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 0, message = "{bike.add.component.usage.invalid}")
    private int usageTimeMinutes;
}
