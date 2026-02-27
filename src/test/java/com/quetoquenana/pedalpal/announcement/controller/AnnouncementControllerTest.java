package com.quetoquenana.pedalpal.announcement.controller;

import com.quetoquenana.pedalpal.announcement.application.query.AnnouncementQueryService;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementStatusUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementApiMapper;
import com.quetoquenana.pedalpal.announcement.presentation.controller.AnnouncementController;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.util.TestAnnouncementData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnnouncementController.class)
@Import({SecurityConfig.class, AnnouncementApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"ADMIN"})
class AnnouncementControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CreateAnnouncementUseCase createUseCase;

    @MockitoBean
    UpdateAnnouncementUseCase updateUseCase;

    @MockitoBean
    UpdateAnnouncementStatusUseCase updateStatusUseCase;

    @MockitoBean
    AnnouncementQueryService queryService;

    @MockitoBean
    AnnouncementApiMapper apiMapper;

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
    void shouldReturn200_whenGetById() throws Exception {
        UUID id = UUID.randomUUID();

        AnnouncementResult result = TestAnnouncementData.result(id);
        AnnouncementResponse response = TestAnnouncementData.response(id);

        when(queryService.getById(id)).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/announcements/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()));

        verify(queryService, times(1)).getById(id);
    }

    @Test
    void shouldReturn404_whenGetByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(queryService.getById(id)).thenThrow(new RecordNotFoundException());

        mockMvc.perform(get("/v1/api/announcements/{id}", id))
                .andExpect(status().isNotFound());

        verify(apiMapper, never()).toResponse(any(AnnouncementResult.class));
    }

    @Test
    void shouldReturn200_whenGetActive() throws Exception {
        UUID id = UUID.randomUUID();

        AnnouncementResult result = TestAnnouncementData.result(id);
        AnnouncementResponse response = TestAnnouncementData.response(id);

        when(queryService.getActive()).thenReturn(List.of(result));
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/announcements/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(id.toString()));

        verify(queryService, times(1)).getActive();
    }

    @Test
    void shouldReturn201_whenCreate() throws Exception {
        UUID id = UUID.randomUUID();

        AnnouncementResult result = TestAnnouncementData.result(id);
        AnnouncementResponse response = TestAnnouncementData.response(id);

        when(apiMapper.toCommand(any(), eq(AUTH_USER_ID))).thenReturn(TestAnnouncementData.createCommand(AUTH_USER_ID));
        when(createUseCase.execute(any())).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(post("/v1/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"subTitle\":\"Subtitle\",\"description\":\"Description\",\"position\":1,\"url\":\"https://example.com\",\"status\":\"ACTIVE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(id.toString()));

        verify(createUseCase, times(1)).execute(any());
    }

    @Test
    void shouldReturn200_whenUpdateStatus() throws Exception {
        UUID id = UUID.randomUUID();

        AnnouncementResult result = TestAnnouncementData.result(id);
        AnnouncementResponse response = TestAnnouncementData.response(id);

        when(apiMapper.toCommand(eq(id), any())).thenReturn(TestAnnouncementData.statusCommand(id, "INACTIVE"));
        when(updateStatusUseCase.execute(any())).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(patch("/v1/api/announcements/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()));

        verify(updateStatusUseCase, times(1)).execute(any());
    }

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void shouldReturn403_whenUserTriesToCreate() throws Exception {
        mockMvc.perform(post("/v1/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"status\":\"ACTIVE\"}"))
                .andExpect(status().isForbidden());

        verify(createUseCase, never()).execute(any());
    }
}

