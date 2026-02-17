package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.domain.model.MaintenanceSuggestion;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MaintenanceSuggestionMapper {

    private final BikeMapper bikeMapper;
    private final SystemCodeMapper systemCodeMapper = new SystemCodeMapper();
    private final MaintenanceSuggestionItemMapper itemMapper = new MaintenanceSuggestionItemMapper();

    public MaintenanceSuggestion toDomain(MaintenanceSuggestionEntity entity) {
        if (entity == null) return null;

        MaintenanceSuggestion domain = MaintenanceSuggestion.builder()
                .id(entity.getId())
                .bike(bikeMapper.toDomain(entity.getBike()))
                .suggestionType(systemCodeMapper.toDomain(entity.getSuggestionType()))
                .suggestionStatus(systemCodeMapper.toDomain(entity.getSuggestionStatus()))
                .aiProvider(entity.getAiProvider())
                .aiModel(entity.getAiModel())
                .rawPrompt(entity.getRawPrompt())
                .rawResponse(entity.getRawResponse())
                .processingAttempts(entity.getProcessingAttempts())
                .lastError(entity.getLastError())
                .items(toDomainItems(entity.getItems()))
                .build();

        return domain;
    }

    public MaintenanceSuggestionEntity toEntity(MaintenanceSuggestion domain) {
        if (domain == null) return null;

        MaintenanceSuggestionEntity entity = MaintenanceSuggestionEntity.builder()
                .id(domain.getId())
                .suggestionType(systemCodeMapper.toEntity(domain.getSuggestionType()))
                .suggestionStatus(systemCodeMapper.toEntity(domain.getSuggestionStatus()))
                .aiProvider(domain.getAiProvider())
                .aiModel(domain.getAiModel())
                .rawPrompt(domain.getRawPrompt())
                .rawResponse(domain.getRawResponse())
                .processingAttempts(domain.getProcessingAttempts())
                .lastError(domain.getLastError())
                .build();

        // items are transient on entity; mapping handled separately when needed
        return entity;
    }

    private List<com.quetoquenana.pedalpal.domain.model.MaintenanceSuggestionItem> toDomainItems(List<com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionItemEntity> items) {
        if (items == null) return null;
        return items.stream().map(itemMapper::toDomain).collect(Collectors.toList());
    }
}

