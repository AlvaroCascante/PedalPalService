package com.quetoquenana.pedalpal.product.application.result;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record ProductPackageResult(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        GeneralStatus status,
        Set<ProductResult> products
) { }
