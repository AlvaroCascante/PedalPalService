package com.quetoquenana.pedalpal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductPackageRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private String status;

    // Optional list of product ids to replace the package contents
    private List<UUID> productIds;
}

