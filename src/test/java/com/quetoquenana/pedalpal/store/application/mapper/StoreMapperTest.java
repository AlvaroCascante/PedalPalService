package com.quetoquenana.pedalpal.store.application.mapper;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreMapperTest {

    private final StoreMapper mapper = new StoreMapper();

    @Test
    void shouldMapStoreWithoutLocations() {
        Store model = Store.builder().id(UUID.randomUUID()).name("Main Store").build();

        StoreResult result = mapper.toResult(model);

        assertEquals(model.getId(), result.id());
        assertEquals("Main Store", result.name());
        assertEquals(0, result.locations().size());
    }

    @Test
    void shouldMapStoreWithLocations() {
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

        StoreResult result = mapper.toResult(model);

        assertEquals(1, result.locations().size());
        assertEquals("Main Location", result.locations().iterator().next().name());
    }
}

