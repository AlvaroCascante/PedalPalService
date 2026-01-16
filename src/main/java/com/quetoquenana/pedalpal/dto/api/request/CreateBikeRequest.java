package com.quetoquenana.pedalpal.dto.api.request;

import jakarta.validation.constraints.Max;
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
    @NotBlank(message = "{bike.create.owner.blank}")
    private String ownerId;

    @NotBlank(message = "{bike.create.name.blank}")
    private String name;

    @Size(max = 100, message = "{bike.create.brand.max}")
    private String brand;

    @Size(max = 100, message = "{bike.create.model.max}")
    private String model;

    @Min(value = 1900, message = "{bike.create.year.invalid}")
    @Max(value = 2100, message = "{bike.create.year.invalid}")
    private Integer year;

    @NotNull(message = "{bike.create.type.required}")
    private String type;

    @Size(max = 100, message = "{bike.create.serial.max}")
    private String serialNumber;

    @Size(max = 1000, message = "{bike.create.notes.max}")
    private String notes;

    private boolean isPublic;
}
