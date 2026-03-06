package com.quetoquenana.pedalpal.appointment.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.handler.InProgressUpdatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetailStatus;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InProgressUpdatesServiceOrderHandlerTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-03-01T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void shouldUseAuthenticatedUserWhenTechnicianIsNull() {
        InProgressUpdatesServiceOrderHandler handler = new InProgressUpdatesServiceOrderHandler(serviceOrderRepository, clock);

        UUID appointmentId = UUID.randomUUID();
        UUID authenticatedUserId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.IN_PROGRESS)
                .requestedServices(List.of())
                .build();

        ServiceOrderDetail detail = ServiceOrderDetail.builder()
                .status(ServiceOrderDetailStatus.PENDING)
                .build();

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .appointmentId(appointmentId)
                .status(ServiceOrderStatus.CREATED)
                .requestedServices(List.of(detail))
                .build();

        when(serviceOrderRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.of(serviceOrder));
        when(serviceOrderRepository.save(any(ServiceOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        handler.handle(
                appointment,
                AppointmentStatus.BIKE_RECEIVED,
                new ChangeAppointmentStatusCommand(
                        appointmentId,
                        authenticatedUserId,
                        "IN_PROGRESS",
                        null,
                        null,
                        null
                ),
                authenticatedUserId
        );

        ArgumentCaptor<ServiceOrder> captor = ArgumentCaptor.forClass(ServiceOrder.class);
        verify(serviceOrderRepository).save(captor.capture());

        ServiceOrder saved = captor.getValue();
        assertEquals(ServiceOrderStatus.IN_PROGRESS, saved.getStatus());
        assertEquals(Instant.parse("2026-03-01T12:00:00Z"), saved.getStartedAt());
        assertEquals(authenticatedUserId, saved.getRequestedServices().getFirst().getTechnicianId());
        assertEquals(ServiceOrderDetailStatus.IN_PROGRESS, saved.getRequestedServices().getFirst().getStatus());
    }
}
