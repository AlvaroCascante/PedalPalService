package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreLocationJpaRepository extends JpaRepository<StoreLocationEntity, UUID> {
}

