package com.quetoquenana.pedalpal.systemCode.infrastructure.adapter;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemCodeQueryPortAdapterTest {

    @Mock
    SystemCodeQueryService queryService;

    @InjectMocks
    SystemCodeQueryPortAdapter adapter;

    @Test
    void shouldDelegateGetById() {
        UUID id = UUID.randomUUID();
        SystemCodeResult result = SystemCodeResult.builder().id(id).build();

        when(queryService.getById(id)).thenReturn(result);

        SystemCodeResult response = adapter.getById(id);

        assertEquals(result, response);
        verify(queryService).getById(id);
    }

    @Test
    void shouldDelegateGetByCategoryAndStatus() {
        SystemCodeResult result = SystemCodeResult.builder().category("COMPONENT_TYPE").build();

        when(queryService.getByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE))
                .thenReturn(List.of(result));

        List<SystemCodeResult> response = adapter.getByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);

        assertEquals(1, response.size());
        assertEquals(result, response.getFirst());
        verify(queryService).getByCategoryAndStatus("COMPONENT_TYPE", GeneralStatus.ACTIVE);
    }
}
