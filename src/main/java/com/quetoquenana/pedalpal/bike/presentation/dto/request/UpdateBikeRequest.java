package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PATCH request DTO for partial bike updates.
 * Semantics:
 * - Missing field => no change
 * - Present field => validate + apply
 * - Explicit null => rejected (400) via JsonSetter(nulls = FAIL)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBikeRequest {

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.name.blank}")
    private String name;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.brand.blank}")
    private String brand;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.model.blank}")
    private String model;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 1900, message = "{bike.update.year.invalid}")
    private Integer year;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.type.blank}")
    private String type;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.serial.blank}")
    private String serialNumber;

    @JsonSetter(nulls = Nulls.FAIL)
    @Size(min = 1, message = "{bike.update.notes.blank}")
    private String notes;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 0, message = "{bike.update.odometer.invalid}")
    private Integer odometerKm;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 0, message = "{bike.update.usage.invalid}")
    private Integer usageTimeMinutes;

    @JsonSetter(nulls = Nulls.FAIL)
    private Boolean isPublic;

    @JsonSetter(nulls = Nulls.FAIL)
    private Boolean isExternalSync;
}
