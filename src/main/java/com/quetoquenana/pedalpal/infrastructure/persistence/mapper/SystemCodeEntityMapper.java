package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity.SystemCodeEntity;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import org.springframework.stereotype.Component;

@Component
public class SystemCodeEntityMapper {

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

