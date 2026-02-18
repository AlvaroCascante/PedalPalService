package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> getById(UUID productId);

    Product save(Product product);

    Product update(UUID productId, Product product);

    void deleteById(UUID productId);
}

