package com.quetoquenana.pedalpal.infrastructure.persistence.product.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findByStatus(GeneralStatus status);
}

