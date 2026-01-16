package com.quetoquenana.pedalpal.dto.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateComponentRequest {
    @NotNull(message = "{component.create.type.required}")
    private String componentType;

    @NotBlank(message = "{component.create.name.blank}")
    private String name;

    @Size(max = 100, message = "{component.create.brand.max}")
    private String brand;

    @Size(max = 100, message = "{component.create.model.max}")
    private String model;

    @Size(max = 1000, message = "{component.create.notes.max}")
    private String notes;
}
