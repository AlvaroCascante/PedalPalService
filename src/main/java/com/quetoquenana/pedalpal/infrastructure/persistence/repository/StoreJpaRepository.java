package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, UUID> {
}

