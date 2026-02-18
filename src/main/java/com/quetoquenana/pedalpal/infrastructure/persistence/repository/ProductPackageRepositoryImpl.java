package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.domain.repository.ProductPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductPackageRepositoryImpl implements ProductPackageRepository {

    private final ProductPackageJpaRepository repository;

    @Override
    public Optional<ProductPackage> getById(UUID productPackageId) {
        return Optional.empty();
    }

    @Override
    public ProductPackage save(ProductPackage productPackage) {
        return null;
    }

    @Override
    public ProductPackage update(UUID productPackageId, ProductPackage productPackage) {
        return null;
    }

    @Override
    public void deleteById(UUID productPackageId) {

    }
}

