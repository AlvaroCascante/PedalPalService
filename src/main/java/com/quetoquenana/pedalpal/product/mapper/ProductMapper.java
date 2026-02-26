package com.quetoquenana.pedalpal.product.mapper;

import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public ProductResult toProductResult(Product model) {
        return new ProductResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                model.getStatus()
        );
    }

    public ProductPackageResult toProductPackageResult(ProductPackage model) {
        return new ProductPackageResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                model.getStatus(),
                model.getProducts()
                        .stream()
                        .map(this::toProductResult)
                        .collect(Collectors.toSet())
        );
    }
}
