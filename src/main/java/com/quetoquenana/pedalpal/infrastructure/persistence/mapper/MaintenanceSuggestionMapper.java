package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.bike.mapper.BikeEntityMapper;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionItemEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionEntity;
import com.quetoquenana.pedalpal.common.domain.model.MaintenanceSuggestion;
import com.quetoquenana.pedalpal.common.domain.model.MaintenanceSuggestionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MaintenanceSuggestionMapper {

    private final BikeEntityMapper bikeEntityMapper;
    private final MaintenanceSuggestionItemMapper itemMapper;

    public MaintenanceSuggestion toDomain(MaintenanceSuggestionEntity entity) {
        if (entity == null) return null;

        MaintenanceSuggestion domain = MaintenanceSuggestion.builder()
                .id(entity.getId())
                .bike(bikeEntityMapper.toBike(entity.getBike()))
                .suggestionType(SystemCodeMapper.toSystemCode(entity.getSuggestionType()))
                .suggestionStatus(SystemCodeMapper.toSystemCode(entity.getSuggestionStatus()))
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
                .suggestionType(SystemCodeMapper.toSystemCodeEntity(domain.getSuggestionType()))
                .suggestionStatus(SystemCodeMapper.toSystemCodeEntity(domain.getSuggestionStatus()))
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

    private List<MaintenanceSuggestionItem> toDomainItems(List<MaintenanceSuggestionItemEntity> items) {
        if (items == null) return null;
        return items.stream().map(itemMapper::toDomain).collect(Collectors.toList());
    }
}

