package com.quetoquenana.pedalpal.store.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreQueryServiceTest {

    @Mock
    StoreMapper mapper;

    @Mock
    StoreRepository repository;

    @InjectMocks
    StoreQueryService service;

    @Test
    void shouldReturnResultWhenGetById() {
        UUID id = UUID.randomUUID();
        Store model = Store.builder().id(id).name("Main Store").build();
        StoreResult result = new StoreResult(id, "Main Store", java.util.Set.of());

        when(repository.getById(id)).thenReturn(Optional.of(model));
        when(mapper.toResult(model)).thenReturn(result);

        StoreResult response = service.getById(id);

        assertEquals(result, response);
        verify(repository).getById(id);
        verify(mapper).toResult(model);
    }

    @Test
    void shouldThrowWhenGetByIdMissing() {
        UUID id = UUID.randomUUID();

        when(repository.getById(id)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> service.getById(id));
        verify(mapper, never()).toResult(org.mockito.ArgumentMatchers.any(Store.class));
    }

    @Test
    void shouldReturnResultsWhenGetAll() {
        Store model = Store.builder().id(UUID.randomUUID()).name("Main Store").build();
        StoreResult result = new StoreResult(model.getId(), model.getName(), java.util.Set.of());

        when(repository.getAll()).thenReturn(List.of(model));
        when(mapper.toResult(model)).thenReturn(result);

        List<StoreResult> response = service.getAll();

        assertEquals(1, response.size());
        assertEquals(result, response.getFirst());
        verify(repository).getAll();
        verify(mapper).toResult(model);
    }
}

