package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateProductRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductRequest;
import com.quetoquenana.pedalpal.model.local.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product createProduct(CreateProductRequest request);

    Product updateProduct(UUID id, UpdateProductRequest request);

    void deleteProduct(UUID id);

    Product findById(UUID productId);

    Page<Product> findByStatusCodes(Pageable pageable, List<String> codes);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllActive(Pageable pageable);

}

