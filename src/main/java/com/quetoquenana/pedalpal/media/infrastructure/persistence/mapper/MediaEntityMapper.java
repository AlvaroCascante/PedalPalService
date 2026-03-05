package com.quetoquenana.pedalpal.media.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.infrastructure.persistence.entity.MediaEntity;

/**
 * Maps media persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class MediaEntityMapper {

    private MediaEntityMapper() {}

    public static Media toModel(MediaEntity entity) {
        Media model = Media.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .referenceId(entity.getReferenceId())
                .referenceType(entity.getReferenceType())
                .mediaType(entity.getMediaType())
                .contentType(entity.getContentType())
                .isPrimary(entity.getIsPrimary())
                .status(entity.getStatus())
                .storageKey(entity.getStorageKey())
                .provider(entity.getProvider())
                .providerAssetId(entity.getProviderAssetId())
                .sizeBytes(entity.getSizeBytes())
                .metadata(entity.getMetadata())
                .title(entity.getName())
                .altText(entity.getAltText())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public static MediaEntity toEntity(Media model) {
        MediaEntity entity = MediaEntity.builder()
                .id(model.getId())
                .ownerId(model.getOwnerId())
                .referenceId(model.getReferenceId())
                .referenceType(model.getReferenceType())
                .mediaType(model.getMediaType())
                .contentType(model.getContentType())
                .isPrimary(model.getIsPrimary())
                .status(model.getStatus())
                .storageKey(model.getStorageKey())
                .provider(model.getProvider())
                .providerAssetId(model.getProviderAssetId())
                .sizeBytes(model.getSizeBytes())
                .metadata(model.getMetadata())
                .name(model.getTitle())
                .altText(model.getAltText())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }
}
