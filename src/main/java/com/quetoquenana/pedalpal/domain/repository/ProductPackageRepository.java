package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.ProductPackage;

import java.util.Optional;
import java.util.UUID;

public interface ProductPackageRepository {

    Optional<ProductPackage> getById(UUID productPackageId);

    ProductPackage save(ProductPackage productPackage);

    ProductPackage update(UUID productPackageId, ProductPackage productPackage);

    void deleteById(UUID productPackageId);
}

