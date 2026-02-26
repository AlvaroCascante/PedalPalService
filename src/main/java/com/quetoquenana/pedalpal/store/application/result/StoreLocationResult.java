package com.quetoquenana.pedalpal.store.application.result;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record StoreLocationResult(
        UUID id,
        String name,
        String website,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String phone,
        String timezone,
        GeneralStatus status
) { }
