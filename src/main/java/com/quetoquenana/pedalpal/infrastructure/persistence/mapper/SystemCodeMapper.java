package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.SystemCodeEntity;
import org.springframework.stereotype.Component;

@Component
public class SystemCodeMapper {

    public static SystemCode toSystemCode(SystemCodeEntity entity) {
        return SystemCode.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .code(entity.getCode())
                .description(entity.getDescription())
                .codeKey(entity.getCodeKey())
                .label(entity.getLabel())
                .isActive(entity.getIsActive())
                .position(entity.getPosition())
                .build();
    }

    public static SystemCodeEntity toSystemCodeEntity(SystemCode model) {
        return SystemCodeEntity.builder()
                .id(model.getId())
                .category(model.getCategory())
                .code(model.getCode())
                .description(model.getDescription())
                .codeKey(model.getCodeKey())
                .label(model.getLabel())
                .isActive(model.getIsActive())
                .position(model.getPosition())
                .build();
    }
}

