package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.domain.model.SystemCode;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.SystemCodeEntity;
import org.springframework.stereotype.Component;

@Component
public class SystemCodeMapper {

    public SystemCode toDomain(SystemCodeEntity entity) {
        return SystemCode.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .code(entity.getCode())
                .label(entity.getLabel())
                .description(entity.getDescription())
                .codeKey(entity.getCodeKey())
                .isActive(entity.getIsActive())
                .position(entity.getPosition())
                .build();
    }

    public SystemCodeEntity toEntity(SystemCode domain) {
        return SystemCodeEntity.builder()
                .id(domain.getId())
                .category(domain.getCategory())
                .code(domain.getCode())
                .label(domain.getLabel())
                .description(domain.getDescription())
                .codeKey(domain.getCodeKey())
                .isActive(domain.getIsActive())
                .position(domain.getPosition())
                .build();
    }
}

