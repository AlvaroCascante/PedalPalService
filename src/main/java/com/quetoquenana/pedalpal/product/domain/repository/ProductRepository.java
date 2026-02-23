package com.quetoquenana.pedalpal.product.domain.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.product.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> getById(UUID id);

    List<Product> getAll();

    List<Product> findByStatus(GeneralStatus status);

    Product save(Product entity);
}