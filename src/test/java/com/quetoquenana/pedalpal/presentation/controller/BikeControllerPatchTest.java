package com.quetoquenana.pedalpal.presentation.controller;

import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.mapper.BikeApiMapper;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.util.TestBikeData;
import com.quetoquenana.pedalpal.util.TestJsonBodies;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;

import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BikeController.class)
@Import({SecurityConfig.class, BikeApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class BikeControllerPatchTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UpdateBikeUseCase updateBikeUseCase;

    @MockitoBean
    MessageSource messageSource;

    @MockitoBean
    CreateBikeUseCase createBikeUseCase;

    @Test
    void shouldReturn200_whenValidPatch() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(eq("authentication.required"), any(), any(Locale.class)))
                .thenReturn("Authentication is required.");

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(updateBikeUseCase.execute(any()))
                .thenReturn(TestBikeData.updateBikeResult(bikeId));

        mockMvc.perform(patch("/v1/api/bikes/{id}", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.patchBikeName("New name")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()))
                .andExpect(jsonPath("$.data.name").value("New name"));
    }

    @Test
    void shouldReturn400_whenBlankName() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(FieldError.class), any(Locale.class)))
                .thenReturn("Name cannot be blank");
        when(messageSource.getMessage(eq("validation.failed"), any(), any(Locale.class)))
                .thenReturn("Validation failed");

        String body = TestJsonBodies.patchBikeName("");

        mockMvc.perform(patch("/v1/api/bikes/{id}", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404_whenBikeNotFound() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(updateBikeUseCase.execute(any()))
                .thenThrow(new RecordNotFoundException("bike.not.found"));

        mockMvc.perform(patch("/v1/api/bikes/{id}", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.patchBikeName("New name")))
                .andExpect(status().isNotFound());
    }
}
