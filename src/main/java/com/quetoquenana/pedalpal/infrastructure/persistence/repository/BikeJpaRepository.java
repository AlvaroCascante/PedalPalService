package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikeJpaRepository extends JpaRepository<BikeEntity, UUID> {
    boolean existsBySerialNumber(String serialNumber);
}
