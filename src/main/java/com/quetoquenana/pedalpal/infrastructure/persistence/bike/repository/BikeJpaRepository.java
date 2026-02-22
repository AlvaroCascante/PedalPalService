package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeJpaRepository extends JpaRepository<BikeEntity, UUID> {
    boolean existsBySerialNumber(String serialNumber);

    Optional<BikeEntity> findByIdAndOwnerId(UUID id, UUID ownerId);

    List<BikeEntity> findByOwnerIdAndStatus(UUID ownerId, BikeStatus bikeStatus);
}

