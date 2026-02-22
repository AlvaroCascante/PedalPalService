package com.quetoquenana.pedalpal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductPackageRequest {

    @NotBlank(message = "{package.create.name.blank}")
    private String name;

    private String description;

    @NotNull(message = "{package.create.price.required}")
    private BigDecimal price;

    // Optional status code string; service will resolve to a SystemCode
    private String status;

    // List of product IDs to include in the package
    private List<UUID> productIds;
}

