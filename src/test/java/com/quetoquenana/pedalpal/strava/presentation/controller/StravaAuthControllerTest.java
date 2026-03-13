package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.strava.application.query.StravaConnectionStatusQuery;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectUrlResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.application.useCase.GetStravaConnectUrlUseCase;
import com.quetoquenana.pedalpal.strava.application.useCase.HandleStravaOAuthCallbackUseCase;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectUrlResponse;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectionStatusResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StravaAuthController.class)
@Import({SecurityConfig.class, StravaApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class StravaAuthControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GetStravaConnectUrlUseCase getStravaConnectUrlUseCase;

    @MockitoBean
    StravaConnectionStatusQuery stravaConnectionStatusQuery;

    @MockitoBean
    HandleStravaOAuthCallbackUseCase handleStravaOAuthCallbackUseCase;

    @MockitoBean
    StravaApiMapper apiMapper;

    @MockitoBean
    AuthenticatedUserPort authenticatedUserPort;

    @MockitoBean
    MessageSource messageSource;

    @BeforeEach
    void setUpAuth() {
        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(AUTH_USER_ID, "test-user", "Test User", UserType.CUSTOMER)));
    }

    @Test
    void shouldReturnConnectUrl() throws Exception {
        StravaConnectUrlResult result = new StravaConnectUrlResult("https://example.com/strava", "state");
        StravaConnectUrlResponse response = new StravaConnectUrlResponse(result.url(), result.state());

        when(getStravaConnectUrlUseCase.execute()).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/strava/connect-url"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.url").value(result.url()));
    }

    @Test
    void shouldReturnConnectionStatus() throws Exception {
        StravaConnectionStatusResult result = new StravaConnectionStatusResult(true, StravaConnectionStatus.CONNECTED, 123L, "read");
        StravaConnectionStatusResponse response = new StravaConnectionStatusResponse(true, StravaConnectionStatus.CONNECTED, 123L, "read");

        when(stravaConnectionStatusQuery.getConnectionStatus()).thenReturn(result);
        when(apiMapper.toResponse(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/strava/connection/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.connected").value(true));
    }
}
