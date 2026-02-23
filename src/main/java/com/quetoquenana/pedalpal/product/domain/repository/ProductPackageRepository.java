package com.quetoquenana.pedalpal.product.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPackageRepository {

    Optional<ProductPackage> getById(UUID id);

    List<ProductPackage> getAll();

    List<ProductPackage> findByStatus(GeneralStatus status);

    ProductPackage save(ProductPackage entity);
}