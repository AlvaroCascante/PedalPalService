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
                .isActive(domain.getIsActive())
                .position(domain.getPosition())
                .build();
    }
}

