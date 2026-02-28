package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BikeHistoryJpaRepository extends JpaRepository<BikeHistoryEntity, UUID> {
    List<BikeHistoryEntity> findByBikeId(UUID bikeId);
}