package com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.entity.SystemCodeEntity;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;

/**
 * Maps SystemCode entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public final class SystemCodeEntityMapper {

    private SystemCodeEntityMapper() {}

    /**
     * Converts a persistence entity into a domain model.
     */
    public static SystemCode toModel(SystemCodeEntity entity) {
        return SystemCode.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .code(entity.getCode())
                .description(entity.getDescription())
                .codeKey(entity.getCodeKey())
                .label(entity.getLabel())
                .status(entity.getStatus())
                .position(entity.getPosition())
                .build();
    }

    /**
     * Converts a domain model into a persistence entity.
     */
    public static SystemCodeEntity toEntity(SystemCode model) {
        return SystemCodeEntity.builder()
                .id(model.getId())
                .category(model.getCategory())
                .code(model.getCode())
                .description(model.getDescription())
                .codeKey(model.getCodeKey())
                .label(model.getLabel())
                .status(model.getStatus())
                .position(model.getPosition())
                .build();
    }
}
