package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.CfImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CfImageJpaRepository extends JpaRepository<CfImageEntity, UUID> {
}

