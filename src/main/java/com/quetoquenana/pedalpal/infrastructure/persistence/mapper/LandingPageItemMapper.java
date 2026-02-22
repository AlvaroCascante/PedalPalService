package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.LandingPageItemEntity;
import com.quetoquenana.pedalpal.common.domain.model.LandingPageItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LandingPageItemMapper {

    public LandingPageItem toDomain(LandingPageItemEntity entity) {
        if (entity == null) return null;

        LandingPageItem domain = LandingPageItem.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .description(entity.getDescription())
                .position(entity.getPosition())
                .url(entity.getUrl())
                .status(SystemCodeMapper.toSystemCode(entity.getStatus()))
                .build();
        return domain;
    }

    public LandingPageItemEntity toEntity(LandingPageItem domain) {
        if (domain == null) return null;

        LandingPageItemEntity entity = LandingPageItemEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .subtitle(domain.getSubtitle())
                .description(domain.getDescription())
                .position(domain.getPosition())
                .url(domain.getUrl())
                .status(SystemCodeMapper.toSystemCodeEntity(domain.getStatus()))
                .build();
        return entity;
    }
}

