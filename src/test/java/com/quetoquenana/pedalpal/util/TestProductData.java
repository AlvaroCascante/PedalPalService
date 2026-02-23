package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductPackageResponse;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public final class TestProductData {

    private TestProductData() {
    }

    public static ProductResult productResult(UUID productId) {
        return ProductResult.builder()
                .id(productId)
                .name("Chain")
                .description("Bike chain")
                .price(new BigDecimal("19.99"))
                .status(GeneralStatus.ACTIVE)
                .build();
    }

    public static ProductResponse productResponse(UUID productId) {
        return new ProductResponse(
                productId,
                "Chain",
                "Bike chain",
                new BigDecimal("19.99"),
                GeneralStatus.ACTIVE
        );
    }

    public static ProductPackageResult packageResult(UUID packageId, UUID productId) {
        return ProductPackageResult.builder()
                .id(packageId)
                .name("Basic Maintenance")
                .description("Basic package")
                .price(new BigDecimal("99.99"))
                .status(GeneralStatus.ACTIVE)
                .products(Set.of(productResult(productId)))
                .build();
    }

    public static ProductPackageResponse packageResponse(UUID packageId, UUID productId) {
        return new ProductPackageResponse(
                packageId,
                "Basic Maintenance",
                "Basic package",
                new BigDecimal("99.99"),
                GeneralStatus.ACTIVE,
                Set.of(productResponse(productId))
        );
    }
}

