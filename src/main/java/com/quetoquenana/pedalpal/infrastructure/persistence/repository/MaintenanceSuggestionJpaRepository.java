package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaintenanceSuggestionJpaRepository extends JpaRepository<MaintenanceSuggestionEntity, UUID> {
}

