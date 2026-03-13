package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.strava.application.query.StravaAthleteQuery;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaAthleteBikeResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StravaController.class)
@Import({SecurityConfig.class, StravaApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class StravaAthleteBikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StravaAthleteQuery stravaAthleteQuery;

    @MockitoBean
    StravaApiMapper apiMapper;

    @MockitoBean
    MessageSource messageSource;

    @Test
    void shouldReturnActiveBikes() throws Exception {
        StravaAthleteBikeResult result = new StravaAthleteBikeResult(
                "gear-1",
                "Bike A",
                "Roadie",
                true,
                false,
                new BigDecimal("123.45")
        );
        StravaAthleteBikeResponse response = new StravaAthleteBikeResponse(
                result.id(),
                result.name(),
                result.nickname(),
                result.primary(),
                result.retired(),
                result.distance()
        );

        when(stravaAthleteQuery.getAthleteBikes()).thenReturn(List.of(result));
        when(apiMapper.toResponsea(result)).thenReturn(response);

        mockMvc.perform(get("/v1/api/strava/bikes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("gear-1"))
                .andExpect(jsonPath("$.data[0].name").value("Bike A"))
                .andExpect(jsonPath("$.data[0].distance").value(123.45));
    }

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"ADMIN"})
    void shouldReturnForbiddenWhenUserRoleMissing() throws Exception {
        mockMvc.perform(get("/v1/api/strava/bikes"))
                .andExpect(status().isForbidden());
    }
}

