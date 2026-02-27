package com.quetoquenana.pedalpal.infrastructure.persistence.product.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.ProductEntityMapper;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    public Optional<Product> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Product> getByIdAndStatus(UUID id, GeneralStatus status) {
        return repository.getByIdAndStatus(id, status).map(mapper::toModel);
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Product> findByStatus(GeneralStatus status) {
        return repository.findByStatus(status).stream().map(mapper::toModel).toList();
    }

    @Override
    public Product save(Product model) {
        ProductEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }
}

