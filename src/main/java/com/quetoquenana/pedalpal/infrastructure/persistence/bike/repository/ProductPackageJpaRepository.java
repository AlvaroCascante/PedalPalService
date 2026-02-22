package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.ProductPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPackageJpaRepository extends JpaRepository<ProductPackageEntity, UUID> {
}

