package com.quetoquenana.pedalpal.media.presentation.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.application.command.ConfirmUploadCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.application.query.MediaQueryService;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.MediaUploadUseCase;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.presentation.dto.request.MediaRequest;
import com.quetoquenana.pedalpal.media.presentation.dto.request.UploadMediaRequest;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
                UUID.randomUUID(),
                mediaId,
                "image/jpeg",
                "r2",
                MediaStatus.ACTIVE,
                "front",
                "Front view",
                "https://cdn.example/media/key",
                java.time.Instant.now().plusSeconds(60),
                true
        );
        MediaResponse response = new MediaResponse(
                mediaId,
                UUID.randomUUID(),
                "image/jpeg",
                "r2",
                "ACTIVE",
                "front",
                "Front view",
                "https://cdn.example/media/key",
                result.expiresAt(),
                true
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

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void shouldReturn200_whenUploadMedia() throws Exception {
        UUID mediaId = UUID.randomUUID();
        UploadMediaRequest request = new UploadMediaRequest(
                true,
                "BIKE",
                List.of(new MediaRequest(UUID.randomUUID(), "image/jpeg", "front.jpg", "Front view"))
        );
        UploadMediaCommand command = new UploadMediaCommand(
                true,
                mediaId,
                MediaReferenceType.BIKE,
                List.of(new UploadMediaSpecCommand(
                        UUID.randomUUID(),"image/jpeg", "front.jpg", "Front view"))
        );
        MediaResult result = new MediaResult(
                UUID.randomUUID(),
                mediaId,
                "image/jpeg",
                "r2",
                MediaStatus.ACTIVE,
                "front",
                "Front view",
                "https://cdn.example/media/key",
                Instant.now().plusSeconds(60),
                true
        );
        MediaResponse response = new MediaResponse(
                mediaId,
                UUID.randomUUID(),
                "image/jpeg",
                "r2",
                "ACTIVE",
                "front",
                "Front view",
                "https://cdn.example/media/key",
                result.expiresAt(),
                true
        );

        when(mapper.toCommand(eq(mediaId), any(UploadMediaRequest.class))).thenReturn(command);
        when(mediaUploadUseCase.execute(command)).thenReturn(List.of(result));
        when(mapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(post("/v1/api/media/{id}", mediaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"isPublic":true,"referenceType":"BIKE","mediaFiles":[{"id":"00000000-0000-0000-0000-000000000002","contentType":"image/jpeg","name":"front.jpg","altText":"Front view"}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(mediaId.toString()))
                .andExpect(jsonPath("$.data[0].url").value("https://cdn.example/media/key"));

        verify(mediaUploadUseCase, times(1)).execute(command);
        verify(mapper, times(1)).toCommand(eq(mediaId), any(UploadMediaRequest.class));
        verify(mapper, times(1)).toResponse(result);
    }

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"ADMIN"})
    void shouldReturn403_whenUserRoleMissing() throws Exception {
        mockMvc.perform(post("/v1/api/media/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"isPublic":true,"referenceType":"BIKE","mediaFiles":[{"id":"00000000-0000-0000-0000-000000000002","contentType":"image/jpeg","name":"front.jpg","altText":"Front view"}]}
                                """))
                .andExpect(status().isForbidden());

        verify(mediaUploadUseCase, never()).execute(any());
        verify(mapper, never()).toCommand(any(UUID.class), any(UploadMediaRequest.class));
    }

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"USER"})
    void shouldReturn400_whenMediaFilesMissing() throws Exception {
        mockMvc.perform(post("/v1/api/media/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"isPublic":true,"referenceType":"BIKE"}
                                """))
                .andExpect(status().isBadRequest());

        verify(mediaUploadUseCase, never()).execute(any());
        verify(mapper, never()).toCommand(any(UUID.class), any(UploadMediaRequest.class));
    }
}
