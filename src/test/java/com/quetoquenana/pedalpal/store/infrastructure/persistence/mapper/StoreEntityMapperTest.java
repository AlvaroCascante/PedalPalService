package com.quetoquenana.pedalpal.store.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreEntity;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreLocationEntity;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreEntityMapperTest {

    @Test
    void shouldMapEntityToModel() {
        UUID storeId = UUID.randomUUID();
        StoreEntity entity = StoreEntity.builder()
                .id(storeId)
                .name("Main Store")
                .build();
        StoreLocationEntity location = StoreLocationEntity.builder()
                .id(UUID.randomUUID())
                .name("Main Location")
                .timezone("CR")
                .status(GeneralStatus.ACTIVE)
                .build();
        entity.addLocation(location);

        Store model = StoreEntityMapper.toModel(entity);

        assertEquals(storeId, model.getId());
        assertEquals("Main Store", model.getName());
        assertEquals(1, model.getLocations().size());
    }

    @Test
    void shouldMapModelToEntity() {
        StoreLocation location = StoreLocation.builder()
                .id(UUID.randomUUID())
                .name("Main Location")
                .status(GeneralStatus.ACTIVE)
                .timezone("CR")
                .build();
        Store model = Store.builder()
                .id(UUID.randomUUID())
                .name("Main Store")
                .locations(Set.of(location))
                .build();

        StoreEntity entity = StoreEntityMapper.toEntity(model);

        assertEquals("Main Store", entity.getName());
        assertEquals(1, entity.getLocations().size());
    }
}
