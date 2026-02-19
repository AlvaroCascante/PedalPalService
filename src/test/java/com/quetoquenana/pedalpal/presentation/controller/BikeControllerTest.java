package com.quetoquenana.pedalpal.presentation.controller;

import com.quetoquenana.pedalpal.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeStatusUseCase;
import com.quetoquenana.pedalpal.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.application.useCase.AddBikeComponentUseCase;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BikeController.class)
@Import({SecurityConfig.class, BikeApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class BikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MessageSource messageSource;

    @MockitoBean
    UpdateBikeUseCase updateBikeUseCase;

    @MockitoBean
    UpdateBikeStatusUseCase updateBikeStatusUseCase;

    @MockitoBean
    CreateBikeUseCase createBikeUseCase;

    @MockitoBean
    BikeQueryService bikeQueryService;

    @MockitoBean
    AddBikeComponentUseCase addBikeComponentUseCase;

    @Test
    void shouldReturn200_whenGetById() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(bikeQueryService.getById(eq(bikeId), any(UUID.class)))
                .thenReturn(TestBikeData.bikeResultQuery(bikeId));

        mockMvc.perform(get("/v1/api/bikes/{id}", bikeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()));
    }

    @Test
    void shouldReturn200_whenFetchActive() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(bikeQueryService.fetchActiveByOwnerId(any(UUID.class)))
                .thenReturn(List.of(TestBikeData.bikeResultQuery(bikeId)));

        mockMvc.perform(get("/v1/api/bikes/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(bikeId.toString()));
    }

    @Test
    void shouldReturn201_whenValidCreate() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(createBikeUseCase.execute(any())).thenReturn(TestBikeData.bikeResult(bikeId));

        mockMvc.perform(post("/v1/api/bikes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.createBikeMinimal("My bike", "ROAD")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()))
                .andExpect(jsonPath("$.data.name").value("My bike"));
    }

    @Test
    void shouldReturn400_whenCreateValidationFails_blankName() throws Exception {
        when(messageSource.getMessage(any(FieldError.class), any(Locale.class)))
                .thenReturn("Name cannot be blank");
        when(messageSource.getMessage(eq("validation.failed"), any(), any(Locale.class)))
                .thenReturn("Validation failed");

        mockMvc.perform(post("/v1/api/bikes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.createBikeMinimal("", "ROAD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenValidPatch() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(updateBikeUseCase.execute(any()))
                .thenReturn(TestBikeData.bikeResultUpdated(bikeId));

        mockMvc.perform(patch("/v1/api/bikes/{id}", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.patchBikeName("New name")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()))
                .andExpect(jsonPath("$.data.name").value("New name"));
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

    @Test
    void shouldReturn200_whenValidStatusPatch() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(updateBikeStatusUseCase.execute(any()))
                .thenReturn(TestBikeData.bikeResultWithStatus(bikeId, "ACTIVE"));

        mockMvc.perform(patch("/v1/api/bikes/{id}/status", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.patchBikeStatus("ACTIVE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400_whenStatusPatchValidationFails_missingStatus() throws Exception {
        when(messageSource.getMessage(any(FieldError.class), any(Locale.class)))
                .thenReturn("Status is required");
        when(messageSource.getMessage(eq("validation.failed"), any(), any(Locale.class)))
                .thenReturn("Validation failed");

        mockMvc.perform(patch("/v1/api/bikes/{id}/status", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404_whenStatusPatchBikeNotFound() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(updateBikeStatusUseCase.execute(any()))
                .thenThrow(new RecordNotFoundException("bike.not.found"));

        mockMvc.perform(patch("/v1/api/bikes/{id}/status", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.patchBikeStatus("ACTIVE")))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn201_whenValidAddBikeComponent() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
                .thenReturn("Road");

        when(addBikeComponentUseCase.execute(any()))
                .thenReturn(TestBikeData.bikeResultUpdated(bikeId));

        mockMvc.perform(post("/v1/api/bikes/{id}/components", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.addBikeComponentMinimal("Chain", "CHAIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(bikeId.toString()));
    }

    @Test
    void shouldReturn400_whenAddBikeComponentValidationFails_missingName() throws Exception {
        when(messageSource.getMessage(any(FieldError.class), any(Locale.class)))
                .thenReturn("Name is required");
        when(messageSource.getMessage(eq("validation.failed"), any(), any(Locale.class)))
                .thenReturn("Validation failed");

        UUID bikeId = UUID.randomUUID();

        mockMvc.perform(post("/v1/api/bikes/{id}/components", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"CHAIN\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404_whenAddBikeComponentBikeNotFound() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(addBikeComponentUseCase.execute(any()))
                .thenThrow(new RecordNotFoundException("bike.not.found"));

        mockMvc.perform(post("/v1/api/bikes/{id}/components", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestJsonBodies.addBikeComponentMinimal("Chain", "CHAIN")))
                .andExpect(status().isNotFound());
    }
}
