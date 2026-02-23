package com.quetoquenana.pedalpal.product.application.result;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductResult(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    GeneralStatus status
) {
}
