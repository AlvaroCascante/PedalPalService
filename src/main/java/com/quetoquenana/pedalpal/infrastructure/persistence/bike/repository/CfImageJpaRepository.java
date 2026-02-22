package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.CfImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CfImageJpaRepository extends JpaRepository<CfImageEntity, UUID> {
}

