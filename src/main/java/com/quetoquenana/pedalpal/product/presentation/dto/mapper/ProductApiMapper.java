package com.quetoquenana.pedalpal.product.presentation.dto.mapper;

import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductPackageResponse;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductApiMapper {
    public ProductResponse toProductResponse(ProductResult result) {
        return new ProductResponse(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                result.status()
        );
    }

    public ProductPackageResponse toProductPackageResponse(ProductPackageResult result) {
        return new ProductPackageResponse(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                result.status(),
                result.products().stream().map(this::toProductResponse).collect(Collectors.toSet())
        );
    }
}
