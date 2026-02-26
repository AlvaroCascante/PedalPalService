package com.quetoquenana.pedalpal.store.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record StoreLocationResponse(
        UUID id,
        String name,
        String website,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String phone,
        String timezone,
        String status
) { }

