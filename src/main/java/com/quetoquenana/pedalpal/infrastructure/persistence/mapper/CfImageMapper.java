package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.CfImageEntity;
import com.quetoquenana.pedalpal.common.domain.model.CfImage;
import org.springframework.stereotype.Component;

@Component
public class CfImageMapper {

    public CfImage toDomain(CfImageEntity entity) {
        if (entity == null) return null;

        CfImage domain = CfImage.builder()
                .id(entity.getId())
                .provider(entity.getProvider())
                .providerAssetId(entity.getProviderAssetId())
                .ownerId(entity.getOwnerId())
                .contextCode(entity.getContextCode())
                .referenceId(entity.getReferenceId())
                .position(entity.getPosition())
                .isPrimary(entity.getIsPrimary())
                .title(entity.getTitle())
                .altText(entity.getAltText())
                .metadata(entity.getMetadata())
                .build();
        return domain;
    }

    public CfImageEntity toEntity(CfImage domain) {
        if (domain == null) return null;

        CfImageEntity entity = CfImageEntity.builder()
                .id(domain.getId())
                .provider(domain.getProvider())
                .providerAssetId(domain.getProviderAssetId())
                .ownerId(domain.getOwnerId())
                .contextCode(domain.getContextCode())
                .referenceId(domain.getReferenceId())
                .position(domain.getPosition())
                .isPrimary(domain.getIsPrimary())
                .title(domain.getTitle())
                .altText(domain.getAltText())
                .metadata(domain.getMetadata())
                .build();
        return entity;
    }
}

