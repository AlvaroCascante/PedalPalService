package com.quetoquenana.pedalpal.media.presentation.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.query.MediaQueryService;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.MediaUploadUseCase;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;
import com.quetoquenana.pedalpal.media.presentation.mapper.MediaApiMapper;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MediaController.class)
@Import({SecurityConfig.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"ADMIN"})
class MediaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MediaUploadUseCase mediaUploadUseCase;

    @MockitoBean
    ConfirmMediaUploadUseCase confirmMediaUploadUseCase;

    @MockitoBean
    MediaApiMapper mapper;

    @MockitoBean
    MessageSource messageSource;

    @MockitoBean
    MediaQueryService queryService;

    @Test
    void shouldReturn200_whenConfirmUpload() throws Exception {
        UUID mediaId = UUID.randomUUID();
        ConfirmUploadCommand command = new ConfirmUploadCommand(mediaId);
        MediaResult result = new MediaResult(
                mediaId,
                "image/jpeg",
                "r2",
                true,
                MediaStatus.ACTIVE,
                "front",
                "Front view",
                "https://cdn.example/media/key",
                java.time.Instant.now().plusSeconds(60)
        );
        MediaResponse response = new MediaResponse(
                mediaId,
                "image/jpeg",
                "r2",
                true,
                "ACTIVE",
                "front",
                "Front view",
                "https://cdn.example/media/key",
                result.expiresAt()
        );

        when(mapper.toCommand(mediaId)).thenReturn(command);
        when(queryService.getById(mediaId)).thenReturn(result);
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(post("/v1/api/media/{id}/confirm", mediaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(mediaId.toString()))
                .andExpect(jsonPath("$.data.url").value("https://cdn.example/media/key"));

        verify(confirmMediaUploadUseCase, times(1)).execute(any());
        verify(queryService, times(1)).getById(mediaId);
        verify(mapper, times(1)).toCommand(mediaId);
        verify(mapper, times(1)).toResponse(result);
    }

    @Test
    @WithAnonymousUser
    void shouldReturn401_whenAuthenticationMissing() throws Exception {
        mockMvc.perform(post("/v1/api/media/{id}/confirm", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(confirmMediaUploadUseCase, never()).execute(any());
        verify(mapper, never()).toCommand(any(UUID.class));
    }
}
