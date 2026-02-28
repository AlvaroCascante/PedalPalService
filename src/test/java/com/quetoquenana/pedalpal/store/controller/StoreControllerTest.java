package com.quetoquenana.pedalpal.store.controller;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.presentation.controller.StoreController;
import com.quetoquenana.pedalpal.store.mapper.StoreApiMapper;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreResponse;
import com.quetoquenana.pedalpal.util.TestStoreData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StoreController.class)
@Import({SecurityConfig.class, StoreApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class StoreControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StoreQueryService queryService;

    @MockitoBean
    StoreApiMapper apiMapper;

    @MockitoBean
    CurrentUserProvider currentUserProvider;

    @MockitoBean
    MessageSource messageSource;

    @BeforeEach
    void setUpAuth() {
        when(currentUserProvider.getCurrentUser())
                .thenReturn(Optional.of(new SecurityUser(AUTH_USER_ID, "test-user", "Test User", false)));
    }

    @Test
    void shouldReturn200_whenGetById_withoutLocationStatusesParam() throws Exception {
        UUID storeId = UUID.randomUUID();

        StoreResult result = TestStoreData.storeResult(storeId);
        StoreResponse response = TestStoreData.storeResponse(storeId);

        when(queryService.getById(storeId)).thenReturn(result);
        when(apiMapper.toResponse(eq(result), any())).thenReturn(response);

        mockMvc.perform(get("/v1/api/stores/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(storeId.toString()))
                .andExpect(jsonPath("$.data.name").value("PedalPal Store"));

        verify(queryService, times(1)).getById(storeId);
        verify(apiMapper, times(1)).toResponse(eq(result), any());
    }

    @Test
    void shouldReturn200_whenGetById_withLocationStatusesParam() throws Exception {
        UUID storeId = UUID.randomUUID();

        StoreResult result = TestStoreData.storeResult(storeId);
        StoreResponse response = TestStoreData.storeResponse(storeId);

        when(queryService.getById(storeId)).thenReturn(result);
        when(apiMapper.toResponse(eq(result), eq(Set.of(GeneralStatus.ACTIVE, GeneralStatus.INACTIVE)))).thenReturn(response);

        mockMvc.perform(get("/v1/api/stores/{id}", storeId)
                        .queryParam("locationStatus", GeneralStatus.ACTIVE.name())
                        .queryParam("locationStatus", GeneralStatus.INACTIVE.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(storeId.toString()));

        verify(queryService, times(1)).getById(storeId);
        verify(apiMapper, times(1)).toResponse(eq(result), eq(Set.of(GeneralStatus.ACTIVE, GeneralStatus.INACTIVE)));
    }

    @Test
    void shouldReturn200_whenFindAll() throws Exception {
        UUID storeId = UUID.randomUUID();

        StoreResult result = TestStoreData.storeResult(storeId);
        StoreResponse response = TestStoreData.storeResponse(storeId);

        when(queryService.getAll()).thenReturn(List.of(result));
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(storeId.toString()));

        verify(queryService, times(1)).getAll();
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    void shouldReturn400_whenLocationStatusParamIsInvalidEnumValue() throws Exception {
        UUID storeId = UUID.randomUUID();

        mockMvc.perform(get("/v1/api/stores/{id}", storeId)
                        .queryParam("locationStatus", "NOT_A_STATUS"))
                .andExpect(status().isBadRequest());

        verify(queryService, never()).getById(any());
    }
}

