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
                .referenceId(entity.getReferenceId())
                .referenceType(entity.getReferenceType())
                .contentType(entity.getContentType())
                .provider(entity.getProvider())
                .isPrimary(entity.getIsPrimary())
                .status(entity.getStatus())
                .storageKey(entity.getStorageKey())
                .name(entity.getName())
                .altText(entity.getAltText())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public static MediaEntity toEntity(Media model) {
        MediaEntity entity = MediaEntity.builder()
                .id(model.getId())
                .referenceId(model.getReferenceId())
                .referenceType(model.getReferenceType())
                .contentType(model.getContentType())
                .provider(model.getProvider())
                .isPrimary(model.getIsPrimary())
                .status(model.getStatus())
                .storageKey(model.getStorageKey())
                .name(model.getName())
                .altText(model.getAltText())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }
}
