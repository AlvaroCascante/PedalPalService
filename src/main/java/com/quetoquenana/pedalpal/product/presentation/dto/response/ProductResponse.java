package com.quetoquenana.pedalpal.product.presentation.dto.response;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        GeneralStatus status
) { }

