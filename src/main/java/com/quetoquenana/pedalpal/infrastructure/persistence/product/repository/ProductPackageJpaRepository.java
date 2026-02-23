package com.quetoquenana.pedalpal.infrastructure.persistence.product.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductPackageJpaRepository extends JpaRepository<ProductPackageEntity, UUID> {
    List<ProductPackageEntity> findByStatus(GeneralStatus status);
}

