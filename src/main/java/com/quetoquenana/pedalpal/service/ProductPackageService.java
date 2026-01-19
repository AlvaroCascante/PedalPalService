package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateProductPackageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductPackageRequest;
import com.quetoquenana.pedalpal.model.data.ProductPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductPackageService {

    Page<ProductPackage> findAll(Pageable pageable);

    ProductPackage findById(UUID id);

    ProductPackage createPackage(CreateProductPackageRequest request);

    ProductPackage updatePackage(UUID id, UpdateProductPackageRequest request);

    void deletePackage(UUID id);

    List<ProductPackage> findByStatus(String statusCode);

    ProductPackage addProduct(UUID packageId, UUID productId);

    ProductPackage removeProduct(UUID packageId, UUID productId);
}

