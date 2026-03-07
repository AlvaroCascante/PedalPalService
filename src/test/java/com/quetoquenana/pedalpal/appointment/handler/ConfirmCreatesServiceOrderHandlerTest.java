package com.quetoquenana.pedalpal.appointment.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.handler.ConfirmCreatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.serviceorder.application.util.ServiceOrderNumberGenerator;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmCreatesServiceOrderHandlerTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @Mock
    private ServiceOrderNumberGenerator numberGenerator;

    @Mock
    private Clock clock;

    @InjectMocks
    private ConfirmCreatesServiceOrderHandler handler;

    @Test
    void shouldReturnExistingOrderNumberWhenAlreadyCreated() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(customerId)
                .bikeId(UUID.randomUUID())
                .status(AppointmentStatus.CONFIRMED)
                .requestedServices(List.of())
                .build();

        ServiceOrder existing = ServiceOrder.builder().orderNumber("SO-2026-000123").build();
        when(serviceOrderRepository.findByAppointmentId(customerId)).thenReturn(Optional.of(existing));

        String result = handler.handle(
                appointment,
                AppointmentStatus.REQUESTED,
                new ChangeAppointmentStatusCommand(customerId, customerId, "CONFIRMED", null, null, null, null),
                UUID.randomUUID()
        );

        assertEquals("SO-2026-000123", result);
        verify(serviceOrderRepository, never()).save(any());
    }

    @Test
    void shouldCreateOrderWhenDoesNotExist() {
        UUID appointmentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .bikeId(UUID.randomUUID())
                .status(AppointmentStatus.CONFIRMED)
                .requestedServices(List.of())
                .build();

        when(serviceOrderRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.empty());
        when(serviceOrderRepository.nextOrderSequence()).thenReturn(1L);
        when(numberGenerator.generate(1L, clock)).thenReturn("SO-2026-000001");

        ArgumentCaptor<ServiceOrder> captor = ArgumentCaptor.forClass(ServiceOrder.class);
        when(serviceOrderRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        String result = handler.handle(
                appointment,
                AppointmentStatus.REQUESTED,
                new ChangeAppointmentStatusCommand(appointmentId, customerId, "CONFIRMED", null, null, null, null),
                UUID.randomUUID()
        );

        assertEquals("SO-2026-000001", result);
        assertEquals("SO-2026-000001", captor.getValue().getOrderNumber());
        verify(serviceOrderRepository).save(any(ServiceOrder.class));
    }
}
