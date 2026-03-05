package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreLocationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreLocationRepositoryImplTest {

    @Mock
    StoreLocationJpaRepository repository;

    @InjectMocks
    StoreLocationRepositoryImpl adapter;

    @Test
    void shouldReturnModelWhenGetById() {
        UUID id = UUID.randomUUID();
        StoreLocationEntity entity = StoreLocationEntity.builder()
                .id(id)
                .name("Main Location")
                .timezone("CR")
                .status(GeneralStatus.ACTIVE)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        StoreLocation model = adapter.getById(id).orElseThrow();

        assertEquals(id, model.getId());
        assertEquals("Main Location", model.getName());
        verify(repository).findById(id);
    }
}
