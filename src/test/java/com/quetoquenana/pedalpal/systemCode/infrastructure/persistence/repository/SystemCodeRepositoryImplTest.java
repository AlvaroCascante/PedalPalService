package com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.entity.SystemCodeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemCodeRepositoryImplTest {

    @Mock
    SystemCodeJpaRepository repository;

    @InjectMocks
    SystemCodeRepositoryImpl adapter;

    @Test
    void shouldReturnModelWhenGetById() {
        UUID id = UUID.randomUUID();
        SystemCodeEntity entity = buildEntity(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        SystemCode model = adapter.getById(id).orElseThrow();

        assertEquals(id, model.getId());
        assertEquals("COMPONENT_TYPE", model.getCategory());
        assertEquals("CHAIN", model.getCode());
        assertEquals(GeneralStatus.ACTIVE, model.getStatus());
        verify(repository).findById(id);
    }

    @Test
    void shouldReturnModelWhenFindByCategoryAndCode() {
        SystemCodeEntity entity = buildEntity(UUID.randomUUID());

        when(repository.findByCategoryAndCode("COMPONENT_TYPE", "CHAIN"))
                .thenReturn(Optional.of(entity));

        SystemCode model = adapter.findByCategoryAndCode("COMPONENT_TYPE", "CHAIN").orElseThrow();

        assertEquals("COMPONENT_TYPE", model.getCategory());
        assertEquals("CHAIN", model.getCode());
        verify(repository).findByCategoryAndCode("COMPONENT_TYPE", "CHAIN");
    }

    @Test
    void shouldReturnModelsWhenFindByCategoryAndStatus() {
        SystemCodeEntity entity = buildEntity(UUID.randomUUID());

        when(repository.findByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE))
                .thenReturn(List.of(entity));

        List<SystemCode> models = adapter.findByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);

        assertEquals(1, models.size());
        assertEquals("CHAIN", models.getFirst().getCode());
        verify(repository).findByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);
    }

    @Test
    void shouldSaveMappedEntity() {
        UUID id = UUID.randomUUID();
        SystemCodeEntity saved = buildEntity(id);
        SystemCode model = buildModel(null);

        when(repository.save(org.mockito.ArgumentMatchers.any(SystemCodeEntity.class))).thenReturn(saved);

        SystemCode response = adapter.save(model);

        assertEquals(id, response.getId());
        verify(repository).save(org.mockito.ArgumentMatchers.any(SystemCodeEntity.class));
    }

    @Test
    void shouldUpdateWithProvidedId() {
        UUID id = UUID.randomUUID();
        SystemCodeEntity saved = buildEntity(id);
        SystemCode model = buildModel(UUID.randomUUID());
        ArgumentCaptor<SystemCodeEntity> captor = ArgumentCaptor.forClass(SystemCodeEntity.class);

        when(repository.save(org.mockito.ArgumentMatchers.any(SystemCodeEntity.class))).thenReturn(saved);

        SystemCode response = adapter.update(id, model);

        assertEquals(id, response.getId());
        verify(repository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
    }

    private SystemCodeEntity buildEntity(UUID id) {
        return SystemCodeEntity.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .status(GeneralStatus.ACTIVE)
                .codeKey("component.chain")
                .label("Chain")
                .description("Chain")
                .position(1)
                .build();
    }

    private SystemCode buildModel(UUID id) {
        return SystemCode.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .status(GeneralStatus.ACTIVE)
                .codeKey("component.chain")
                .label("Chain")
                .description("Chain")
                .position(1)
                .build();
    }
}
