package com.quetoquenana.pedalpal.product.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.product.infrastructure.persistence.entity.ProductPackageEntity;
import com.quetoquenana.pedalpal.product.mapper.ProductEntityMapper;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductPackageRepositoryImpl implements ProductPackageRepository {

    private final ProductPackageJpaRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    public Optional<ProductPackage> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<ProductPackage> getAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<ProductPackage> getByIdAndStatus(UUID id, GeneralStatus status) {
        return repository.findByIdAndStatus(id, status).map(mapper::toModel);
    }

    @Override
    public List<ProductPackage> findByStatus(GeneralStatus status) {
        return repository.findByStatus(status).stream().map(mapper::toModel).toList();
    }

    @Override
    public ProductPackage save(ProductPackage model) {
        ProductPackageEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }
}

