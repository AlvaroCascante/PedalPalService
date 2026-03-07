package com.quetoquenana.pedalpal.appointment.controller;

import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ChangeAppointmentStatusResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.ChangeAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.presentation.controller.AppointmentController;
import com.quetoquenana.pedalpal.appointment.presentation.mapper.AppointmentApiMapper;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    ChangeAppointmentStatusUseCase changeAppointmentStatusUseCase;

    @MockitoBean
    AppointmentQueryService appointmentQueryService;

    @MockitoBean
    AuthenticatedUserPort currentUserProvider;

    @MockitoBean
    MessageSource messageSource;

    @BeforeEach
    void setUpAuth() {
        when(currentUserProvider.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(AUTH_USER_ID, "test-user", "Test User", UserType.CUSTOMER)));

        when(messageSource.getMessage(any(String.class), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldReturn201_whenValidCreate() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        AppointmentResult result = new AppointmentResult(
                appointmentId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.parse("2026-02-25T10:00:00Z"),
                AppointmentStatus.REQUESTED,
                "notes",
                new BigDecimal("19.99"),
                List.of()
        );

        when(createAppointmentUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(post("/v1/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bikeId\":\"00000000-0000-0000-0000-000000000010\",\"storeLocationId\":\"00000000-0000-0000-0000-000000000020\",\"scheduledAt\":\"2026-02-25T10:00:00Z\",\"notes\":\"notes\",\"requestedServices\":[]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(appointmentId.toString()));
    }

    @Test
    void shouldReturn200_whenChangeStatus() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        ChangeAppointmentStatusResult result = new ChangeAppointmentStatusResult(
                appointmentId,
                AppointmentStatus.REQUESTED,
                AppointmentStatus.CONFIRMED,
                Instant.parse("2026-02-25T10:00:00Z"),
                "SO-2026-000123",
                new BigDecimal("19.99")
        );

        when(changeAppointmentStatusUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(post("/v1/api/appointments/{id}/status", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toStatus\":\"CONFIRMED\",\"closureReason\":null,\"technicianId\":null,\"note\":\"ok\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.appointmentId").value(appointmentId.toString()))
                .andExpect(jsonPath("$.data.serviceOrderNumber").value("SO-2026-000123"));
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
                        new AppointmentListItemResult(
                                UUID.randomUUID(),
                                bikeId,
                                UUID.randomUUID(),
                                Instant.parse("2026-02-25T10:00:00Z"),
                                AppointmentStatus.CONFIRMED
                        )
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

        verify(appointmentQueryService, never()).getById(any());
    }
}
