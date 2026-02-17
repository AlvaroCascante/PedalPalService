package com.quetoquenana.pedalpal.presentation.dto.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "{product.create.name.blank}")
    private String name;

    @Size(max = 1000, message = "{product.create.description.max}")
    private String description;

    @NotNull(message = "{product.create.price.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{product.create.price.invalid}")
    private BigDecimal price;

    // Optional: allow passing a status code or id at create time; services will resolve to SystemCode
    private String status;
}

