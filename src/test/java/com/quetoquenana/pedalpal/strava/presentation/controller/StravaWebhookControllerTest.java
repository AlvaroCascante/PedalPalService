package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.strava.application.useCase.ProcessStravaWebhookUseCase;
import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StravaWebhookController.class)
@Import({SecurityConfig.class, StravaApiMapper.class})
class StravaWebhookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProcessStravaWebhookUseCase processStravaWebhookUseCase;

    @MockitoBean
    StravaProperties stravaProperties;

    @MockitoBean
    StravaApiMapper apiMapper;

    @MockitoBean
    MessageSource messageSource;

    @Test
    void shouldVerifyWebhook() throws Exception {
        when(stravaProperties.getWebhookVerifyToken()).thenReturn("token");

        mockMvc.perform(get("/v1/api/strava/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "token")
                        .param("hub.challenge", "challenge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['hub.challenge']").value("challenge"));
    }

    @Test
    void shouldRejectInvalidWebhookToken() throws Exception {
        when(stravaProperties.getWebhookVerifyToken()).thenReturn("token");

        mockMvc.perform(get("/v1/api/strava/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "invalid")
                        .param("hub.challenge", "challenge"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAcceptWebhookEvent() throws Exception {
        mockMvc.perform(post("/v1/api/strava/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"object_type\":\"activity\",\"aspect_type\":\"create\",\"object_id\":123,\"owner_id\":456,\"event_time\":1710000000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Webhook received"));
    }
}
