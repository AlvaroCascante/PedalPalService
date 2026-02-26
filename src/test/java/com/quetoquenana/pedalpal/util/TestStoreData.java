package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreLocationResponse;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreResponse;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public final class TestStoreData {

    private TestStoreData() {
    }

    public static StoreResult storeResult(UUID storeId) {
        return StoreResult.builder()
                .id(storeId)
                .name("PedalPal Store")
                .locations(Set.of(
                        StoreLocationResult.builder()
                                .id(UUID.randomUUID())
                                .name("Main Location")
                                .website("https://example.com")
                                .address("123 Main St")
                                .latitude(new BigDecimal("9.928069"))
                                .longitude(new BigDecimal("-84.090725"))
                                .phone("+506 2222-2222")
                                .status(GeneralStatus.ACTIVE)
                                .build(),
                        StoreLocationResult.builder()
                                .id(UUID.randomUUID())
                                .name("Inactive Location")
                                .status(GeneralStatus.INACTIVE)
                                .build()
                ))
                .build();
    }

    public static StoreResponse storeResponse(UUID storeId) {
        return new StoreResponse(
                storeId,
                "PedalPal Store",
                Set.of(
                        new StoreLocationResponse(
                                UUID.randomUUID(),
                                "Main Location",
                                "https://example.com",
                                "123 Main St",
                                new BigDecimal("9.928069"),
                                new BigDecimal("-84.090725"),
                                "+506 2222-2222",
                                "CR",
                                GeneralStatus.ACTIVE.name()
                        )
                )
        );
    }
}

