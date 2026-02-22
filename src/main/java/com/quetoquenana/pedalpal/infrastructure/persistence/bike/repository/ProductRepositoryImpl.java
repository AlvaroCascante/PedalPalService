package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.common.domain.model.Product;
import com.quetoquenana.pedalpal.common.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository repository;

    @Override
    public Optional<Product> getById(UUID productId) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public Product update(UUID productId, Product product) {
        return null;
    }

    @Override
    public void deleteById(UUID productId) {

    }
}
