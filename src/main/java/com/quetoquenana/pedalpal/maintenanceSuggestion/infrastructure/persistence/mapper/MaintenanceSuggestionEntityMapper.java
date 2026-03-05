package com.quetoquenana.pedalpal.maintenanceSuggestion.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model.MaintenanceSuggestion;
import com.quetoquenana.pedalpal.maintenanceSuggestion.infrastructure.persistence.entity.MaintenanceSuggestionEntity;

/**
 * Maps MaintenanceSugges persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class MaintenanceSuggestionEntityMapper {

    private MaintenanceSuggestionEntityMapper() {}

    public static MaintenanceSuggestionEntity toEntity(MaintenanceSuggestion model) {
        MaintenanceSuggestionEntity entity = MaintenanceSuggestionEntity.builder()
                .id(model.getId())
                .bikeId(model.getBikeId())
                .suggestionType(model.getSuggestionType())
                .status(model.getStatus())
                .aiProvider(model.getAiProvider())
                .aiModel(model.getAiModel())
                .rawPrompt(model.getRawPrompt())
                .rawResponse(model.getRawResponse())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }

    public static MaintenanceSuggestion toModel(MaintenanceSuggestionEntity  entity) {
        MaintenanceSuggestion domain = MaintenanceSuggestion.builder()
                .id(entity.getId())
                .bikeId(entity.getBikeId())
                .suggestionType(entity.getSuggestionType())
                .status(entity.getStatus())
                .aiProvider(entity.getAiProvider())
                .aiModel(entity.getAiModel())
                .rawPrompt(entity.getRawPrompt())
                .rawResponse(entity.getRawResponse())
                .build();
        domain.setVersion(entity.getVersion());
        return domain;
     }
}
