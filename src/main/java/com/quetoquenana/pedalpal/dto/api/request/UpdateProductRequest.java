package com.quetoquenana.pedalpal.dto.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String name;

    @Size(max = 1000, message = "{product.create.description.max}")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "{product.create.price.invalid}")
    private BigDecimal price;

    private String status;
}

