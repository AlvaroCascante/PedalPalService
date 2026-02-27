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
        return new StoreResult(
                storeId,
                "PedalPal Store",
                Set.of(
                        new StoreLocationResult(
                                UUID.randomUUID(),
                                "Main Location",
                                "https://example.com",
                                "123 Main St",
                                new BigDecimal("9.928069"),
                                new BigDecimal("-84.090725"),
                                "+506 2222-2222",
                                "CR",
                                GeneralStatus.ACTIVE
                        ),
                        new StoreLocationResult(
                                UUID.randomUUID(),
                                "Inactive Location",
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                GeneralStatus.INACTIVE
                        )
                )
        );
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
