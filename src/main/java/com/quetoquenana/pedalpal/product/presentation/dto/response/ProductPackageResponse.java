package com.quetoquenana.pedalpal.product.presentation.dto.response;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductPackageResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String status,
        Set<ProductResponse> products
) { }
