package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.SystemCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SystemCodeJpaRepository extends JpaRepository<SystemCodeEntity, UUID> {

    Optional<SystemCodeEntity> findByCategoryAndCode(String category, String code);
}

