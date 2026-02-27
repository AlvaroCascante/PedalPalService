package com.quetoquenana.pedalpal.systemCode.controller;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.mapper.SystemCodeApiMapper;
import com.quetoquenana.pedalpal.systemCode.presentation.controller.SystemCodeController;
import com.quetoquenana.pedalpal.systemCode.presentation.dto.response.SystemCodeResponse;
import com.quetoquenana.pedalpal.util.TestSystemCodeData;
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

@WebMvcTest(controllers = SystemCodeController.class)
@Import({SecurityConfig.class, SystemCodeApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class SystemCodeControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SystemCodeQueryService queryService;

    @MockitoBean
    SystemCodeApiMapper apiMapper;

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
    void shouldReturn200_whenGetComponentById() throws Exception {
        UUID id = UUID.randomUUID();

        SystemCodeResult result = TestSystemCodeData.systemCodeResult(id);
        SystemCodeResponse response = TestSystemCodeData.systemCodeResponse(id);

        when(queryService.getById(id)).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/system-codes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.code").value("CHAIN"));

        verify(queryService, times(1)).getById(id);
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    void shouldReturn404_whenGetComponentByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(queryService.getById(id)).thenThrow(new RecordNotFoundException());

        mockMvc.perform(get("/v1/api/components/{id}", id))
                .andExpect(status().isNotFound());

        verify(apiMapper, never()).toResponse(any(SystemCodeResult.class));
    }

    @Test
    void shouldReturn200_whenGetActiveComponents() throws Exception {
        UUID id = UUID.randomUUID();

        SystemCodeResult result = TestSystemCodeData.systemCodeResult(id);
        SystemCodeResponse response = TestSystemCodeData.systemCodeResponse(id);

        when(queryService.getActiveComponents()).thenReturn(List.of(result));
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/components"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(id.toString()));

        verify(queryService, times(1)).getActiveComponents();
        verify(apiMapper, times(1)).toResponse(result);
    }

    @Test
    @WithMockJwt(userId = "", roles = {})
    void shouldReturn403_whenMissingRoleForProtectedEndpoint() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/v1/api/system-codes/{id}", id))
                .andExpect(status().isForbidden());

        verify(queryService, never()).getById(eq(id));
    }
}

