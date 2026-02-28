package com.quetoquenana.pedalpal.maintenanceSuggestion.mapper;

import com.quetoquenana.pedalpal.maintenanceSuggestion.infrastructure.persistence.entity.MaintenanceSuggestionEntity;
import com.quetoquenana.pedalpal.maintenanceSuggestion.domain.model.MaintenanceSuggestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceSuggestionEntityMapper {
    public MaintenanceSuggestionEntity toEntity(MaintenanceSuggestion model) {
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

    public MaintenanceSuggestion toModel(MaintenanceSuggestionEntity  entity) {
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
