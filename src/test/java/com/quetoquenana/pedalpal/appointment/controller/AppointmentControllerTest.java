package com.quetoquenana.pedalpal.appointment.controller;

import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.presentation.controller.AppointmentController;
import com.quetoquenana.pedalpal.appointment.presentation.dto.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.application.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class)
@Import({SecurityConfig.class, AppointmentApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001")
class AppointmentControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CreateAppointmentUseCase createAppointmentUseCase;

    @MockitoBean
    UpdateAppointmentUseCase updateAppointmentUseCase;

    @MockitoBean
    UpdateAppointmentStatusUseCase updateAppointmentStatusUseCase;

    @MockitoBean
    AppointmentQueryService appointmentQueryService;

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
    void shouldReturn201_whenValidCreate() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        AppointmentResult result = AppointmentResult.builder()
                .id(appointmentId)
                .bikeId(UUID.randomUUID())
                .storeLocationId(UUID.randomUUID())
                .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                .status(AppointmentStatus.REQUESTED)
                .notes("notes")
                .requestedServices(List.of())
                .build();

        when(createAppointmentUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(post("/v1/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bikeId\":\"00000000-0000-0000-0000-000000000010\",\"storeLocationId\":\"00000000-0000-0000-0000-000000000020\",\"scheduledAt\":\"2026-02-25T10:00:00Z\",\"notes\":\"notes\",\"requestedServices\":[]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(appointmentId.toString()));
    }

    @Test
    void shouldReturn404_whenGetByIdNotFound() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        when(appointmentQueryService.getById(appointmentId))
                .thenThrow(new RecordNotFoundException("appointment.not.found"));

        mockMvc.perform(get("/v1/api/appointments/{id}", appointmentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenGetUpcomingByBikeId() throws Exception {
        UUID bikeId = UUID.randomUUID();

        when(appointmentQueryService.getUpcomingAppointments(eq(bikeId)))
                .thenReturn(List.of(
                        AppointmentListItemResult.builder()
                                .id(UUID.randomUUID())
                                .bikeId(bikeId)
                                .storeLocationId(UUID.randomUUID())
                                .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                                .status(AppointmentStatus.CONFIRMED)
                                .build()
                ));

        mockMvc.perform(get("/v1/api/appointments/bike/{bikeId}/upcoming", bikeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].bikeId").value(bikeId.toString()));
    }

    @Test
    @WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {})
    void shouldReturn403_whenMissingRole() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        mockMvc.perform(get("/v1/api/appointments/{id}", appointmentId))
                .andExpect(status().isForbidden());

        org.mockito.Mockito.verify(appointmentQueryService, never()).getById(any());
    }
}
