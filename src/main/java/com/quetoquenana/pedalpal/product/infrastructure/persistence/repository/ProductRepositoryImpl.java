package com.quetoquenana.pedalpal.product.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.product.infrastructure.persistence.entity.ProductEntity;
import com.quetoquenana.pedalpal.product.infrastructure.persistence.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository repository;

    @Override
    public Optional<Product> getById(UUID id) {
        return repository.findById(id).map(ProductEntityMapper::toModel);
    }

    @Override
    public Optional<Product> getByIdAndStatus(UUID id, GeneralStatus status) {
        return repository.getByIdAndStatus(id, status).map(ProductEntityMapper::toModel);
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll().stream().map(ProductEntityMapper::toModel).toList();
    }

    @Override
    public List<Product> findByStatus(GeneralStatus status) {
        return repository.findByStatus(status).stream().map(ProductEntityMapper::toModel).toList();
    }

    @Override
    public Product save(Product model) {
        ProductEntity entity = ProductEntityMapper.toEntity(model);
        return ProductEntityMapper.toModel(repository.save(entity));
    }
}

