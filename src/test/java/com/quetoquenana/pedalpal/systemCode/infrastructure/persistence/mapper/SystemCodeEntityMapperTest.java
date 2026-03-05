package com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.entity.SystemCodeEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SystemCodeEntityMapperTest {

    @Test
    void shouldMapEntityToModel() {
        UUID id = UUID.randomUUID();
        SystemCodeEntity entity = SystemCodeEntity.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .label("Chain")
                .description("Chain")
                .codeKey("component.chain")
                .status(GeneralStatus.ACTIVE)
                .position(2)
                .build();

        SystemCode model = SystemCodeEntityMapper.toModel(entity);

        assertEquals(id, model.getId());
        assertEquals("COMPONENT_TYPE", model.getCategory());
        assertEquals("CHAIN", model.getCode());
        assertEquals("Chain", model.getLabel());
        assertEquals("Chain", model.getDescription());
        assertEquals("component.chain", model.getCodeKey());
        assertEquals(GeneralStatus.ACTIVE, model.getStatus());
        assertEquals(2, model.getPosition());
    }

    @Test
    void shouldMapModelToEntity() {
        UUID id = UUID.randomUUID();
        SystemCode model = SystemCode.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .label("Chain")
                .description("Chain")
                .codeKey("component.chain")
                .status(GeneralStatus.ACTIVE)
                .position(2)
                .build();

        SystemCodeEntity entity = SystemCodeEntityMapper.toEntity(model);

        assertEquals(id, entity.getId());
        assertEquals("COMPONENT_TYPE", entity.getCategory());
        assertEquals("CHAIN", entity.getCode());
        assertEquals("Chain", entity.getLabel());
        assertEquals("Chain", entity.getDescription());
        assertEquals("component.chain", entity.getCodeKey());
        assertEquals(GeneralStatus.ACTIVE, entity.getStatus());
        assertEquals(2, entity.getPosition());
    }
}

