package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreEntity;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreLocationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class StoreRepositoryImplTest {

    @Mock
    StoreJpaRepository repository;

    @InjectMocks
    StoreRepositoryImpl adapter;

    @Test
    void shouldReturnModelWhenGetById() {
        UUID id = UUID.randomUUID();
        StoreEntity entity = buildEntity(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Store model = adapter.getById(id).orElseThrow();

        assertEquals(id, model.getId());
        assertEquals("Main Store", model.getName());
        verify(repository).findById(id);
    }

    @Test
    void shouldReturnModelsWhenGetAll() {
        StoreEntity entity = buildEntity(UUID.randomUUID());

        when(repository.findAll()).thenReturn(List.of(entity));

        List<Store> models = adapter.getAll();

        assertEquals(1, models.size());
        assertEquals("Main Store", models.getFirst().getName());
        verify(repository).findAll();
    }

    @Test
    void shouldSaveMappedEntity() {
        UUID id = UUID.randomUUID();
        StoreEntity saved = buildEntity(id);
        Store model = Store.builder().id(null).name("Main Store").build();

        when(repository.save(org.mockito.ArgumentMatchers.any(StoreEntity.class))).thenReturn(saved);

        Store response = adapter.save(model);

        assertEquals(id, response.getId());
        verify(repository).save(org.mockito.ArgumentMatchers.any(StoreEntity.class));
    }

    private StoreEntity buildEntity(UUID id) {
        StoreEntity entity = StoreEntity.builder()
                .id(id)
                .name("Main Store")
                .build();
        StoreLocationEntity location = StoreLocationEntity.builder()
                .id(UUID.randomUUID())
                .name("Main Location")
                .timezone("CR")
                .status(GeneralStatus.ACTIVE)
                .build();
        entity.addLocation(location);
        return entity;
    }
}
