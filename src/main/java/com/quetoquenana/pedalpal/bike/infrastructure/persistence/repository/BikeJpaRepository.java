package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeJpaRepository extends JpaRepository<BikeEntity, UUID> {
    boolean existsBySerialNumber(String serialNumber);

    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);

    Optional<BikeEntity> findByIdAndOwnerId(UUID id, UUID ownerId);

    List<BikeEntity> findByOwnerIdAndStatus(UUID ownerId, BikeStatus bikeStatus);
}

