package com.quetoquenana.pedalpal.systemCode.application.query;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.systemCode.application.mapper.SystemCodeMapper;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemCodeQueryServiceTest {

    @Mock
    SystemCodeMapper mapper;

    @Mock
    SystemCodeRepository repository;

    @InjectMocks
    SystemCodeQueryService service;

    @Test
    void shouldReturnResultWhenGetById() {
        UUID id = UUID.randomUUID();
        SystemCode model = SystemCode.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .status(GeneralStatus.ACTIVE)
                .build();
        SystemCodeResult result = SystemCodeResult.builder().id(id).build();

        when(repository.getById(id)).thenReturn(Optional.of(model));
        when(mapper.toResult(model)).thenReturn(result);

        SystemCodeResult response = service.getById(id);

        assertEquals(result, response);
        verify(repository).getById(id);
        verify(mapper).toResult(model);
    }

    @Test
    void shouldThrowWhenGetByIdMissing() {
        UUID id = UUID.randomUUID();

        when(repository.getById(id)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> service.getById(id));
        verify(mapper, never()).toResult(org.mockito.ArgumentMatchers.any(SystemCode.class));
    }

    @Test
    void shouldReturnResultsForCategoryAndStatus() {
        SystemCode model = SystemCode.builder()
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .status(GeneralStatus.ACTIVE)
                .build();
        SystemCodeResult result = SystemCodeResult.builder().category("COMPONENT_TYPE").code("CHAIN").build();

        when(repository.findByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE))
                .thenReturn(List.of(model));
        when(mapper.toResult(model)).thenReturn(result);

        List<SystemCodeResult> response = service.getByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);

        assertEquals(1, response.size());
        assertEquals(result, response.getFirst());
        verify(repository).findByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);
        verify(mapper).toResult(model);
    }

    @Test
    void shouldReturnActiveComponents() {
        SystemCode model = SystemCode.builder()
                .category(COMPONENT_TYPE)
                .code("CHAIN")
                .status(GeneralStatus.ACTIVE)
                .build();
        SystemCodeResult result = SystemCodeResult.builder().category(COMPONENT_TYPE).code("CHAIN").build();

        when(repository.findByCategoryAndStatus(COMPONENT_TYPE, GeneralStatus.ACTIVE))
                .thenReturn(List.of(model));
        when(mapper.toResult(model)).thenReturn(result);

        List<SystemCodeResult> response = service.getActiveComponents();

        assertEquals(1, response.size());
        assertEquals(result, response.getFirst());
        verify(repository).findByCategoryAndStatus(COMPONENT_TYPE, GeneralStatus.ACTIVE);
    }
}
