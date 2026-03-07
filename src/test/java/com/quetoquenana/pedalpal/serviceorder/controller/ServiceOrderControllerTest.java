package com.quetoquenana.pedalpal.serviceorder.controller;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.config.SecurityConfig;
import com.quetoquenana.pedalpal.presentation.security.WithMockJwt;
import com.quetoquenana.pedalpal.serviceorder.application.query.GetServiceOrderCommentsQueryService;
import com.quetoquenana.pedalpal.serviceorder.application.query.ServiceOrderQueryService;
import com.quetoquenana.pedalpal.serviceorder.application.result.ChangeServiceOrderStatusResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.AddServiceOrderCommentUseCase;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.ChangeServiceOrderStatusUseCase;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.presentation.controller.ServiceOrderController;
import com.quetoquenana.pedalpal.serviceorder.presentation.mapper.ServiceOrderApiMapper;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ServiceOrderController.class)
@Import({SecurityConfig.class, ServiceOrderApiMapper.class})
@WithMockJwt(userId = "00000000-0000-0000-0000-000000000001", roles = {"TECHNICIAN"})
class ServiceOrderControllerTest {

    private static final UUID AUTH_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ChangeServiceOrderStatusUseCase changeServiceOrderStatusUseCase;

    @MockitoBean
    AuthenticatedUserPort currentUserProvider;

    @MockitoBean
    ServiceOrderQueryService queryService;

    @MockitoBean
    MessageSource messageSource;

    @MockitoBean
    AddServiceOrderCommentUseCase addServiceOrderCommentUseCase;

    @MockitoBean
    GetServiceOrderCommentsQueryService commentQueryService;

    @BeforeEach
    void setUp() {
        when(currentUserProvider.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(AUTH_USER_ID, "tech-user", "Tech User", UserType.TECHNICIAN)));
        when(messageSource.getMessage(any(String.class), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldReturn200WhenStatusChangeIsValid() throws Exception {
        UUID serviceOrderId = UUID.randomUUID();

        ChangeServiceOrderStatusResult result = new ChangeServiceOrderStatusResult(
                serviceOrderId,
                "SO-2026-000123",
                ServiceOrderStatus.CREATED,
                ServiceOrderStatus.IN_PROGRESS,
                Instant.parse("2026-03-06T10:00:00Z"),
                null
        );

        when(changeServiceOrderStatusUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(post("/v1/api/service-orders/{id}/status", serviceOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toStatus\":\"IN_PROGRESS\",\"technicianId\":null,\"note\":\"Starting work\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.serviceOrderId").value(serviceOrderId.toString()))
                .andExpect(jsonPath("$.data.orderNumber").value("SO-2026-000123"));
    }

    @Test
    void shouldReturn400WhenStatusIsBlank() throws Exception {
        UUID serviceOrderId = UUID.randomUUID();

        mockMvc.perform(post("/v1/api/service-orders/{id}/status", serviceOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toStatus\":\"\",\"technicianId\":null,\"note\":\"Starting work\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenCommentAdded() throws Exception {
        UUID serviceOrderId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        ServiceOrderCommentResult result = new ServiceOrderCommentResult(
                commentId,
                serviceOrderId,
                "Initial diagnostics completed",
                true,
                UserType.TECHNICIAN,
                Instant.parse("2026-03-06T10:15:00Z"),
                AUTH_USER_ID
        );

        when(addServiceOrderCommentUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(post("/v1/api/service-orders/{id}/comments", serviceOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Initial diagnostics completed\",\"customerVisible\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(commentId.toString()))
                .andExpect(jsonPath("$.data.comment").value("Initial diagnostics completed"))
                .andExpect(jsonPath("$.data.createdByType").value("TECHNICIAN"));
    }

    @Test
    void shouldReturn400WhenCommentIsBlank() throws Exception {
        UUID serviceOrderId = UUID.randomUUID();

        mockMvc.perform(post("/v1/api/service-orders/{id}/comments", serviceOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"\",\"customerVisible\":true}"))
                .andExpect(status().isBadRequest());

        verify(addServiceOrderCommentUseCase, never()).execute(any());
    }

    @Test
    void shouldReturn200WhenFetchingComments() throws Exception {
        UUID serviceOrderId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        ServiceOrderCommentResult result = new ServiceOrderCommentResult(
                commentId,
                serviceOrderId,
                "Customer approved estimate",
                true,
                UserType.TECHNICIAN,
                Instant.parse("2026-03-06T11:00:00Z"),
                AUTH_USER_ID
        );

        when(commentQueryService.getByServiceOrderId(serviceOrderId)).thenReturn(List.of(result));

        mockMvc.perform(get("/v1/api/service-orders/{id}/comments", serviceOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(commentId.toString()))
                .andExpect(jsonPath("$.data[0].comment").value("Customer approved estimate"));
    }
}
