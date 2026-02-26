package com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity.SystemCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemCodeJpaRepository extends JpaRepository<SystemCodeEntity, UUID> {

    Optional<SystemCodeEntity> findByCategoryAndCode(String category, String code);

    List<SystemCodeEntity> findByCategoryAndStatus(String category, GeneralStatus status);
}

