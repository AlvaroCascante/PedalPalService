package com.quetoquenana.pedalpal.appointment.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.ConfirmAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.model.RequestedService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmAppointmentUseCaseTest {

    @Mock
    AppointmentMapper mapper;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    ServiceOrderMapper serviceOrderMapper;

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @InjectMocks
    ConfirmAppointmentUseCase useCase;

    @Captor
    ArgumentCaptor<ServiceOrder> serviceOrderCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldConfirmAppointment_andCreateServiceOrder() {
            UUID appointmentId = UUID.randomUUID();

            Appointment appointment = Appointment.builder()
                    .id(appointmentId)
                    .bikeId(UUID.randomUUID())
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .requestedServices(new ArrayList<>())
                    .build();

            when(appointmentRepository.getById(appointmentId)).thenReturn(Optional.of(appointment));
            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0, Appointment.class));

            AppointmentResult appointmentResult = new AppointmentResult(
                    appointmentId,
                    appointment.getBikeId(),
                    appointment.getStoreLocationId(),
                    appointment.getScheduledAt(),
                    AppointmentStatus.CONFIRMED,
                    appointment.getNotes(),
                    List.of()
            );

            when(mapper.toResult(any(Appointment.class))).thenReturn(appointmentResult);

            ServiceOrder savedOrder = ServiceOrder.builder().id(UUID.randomUUID()).build();
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(savedOrder);

            ServiceOrderResult serviceOrderResult = new ServiceOrderResult(
                    savedOrder.getId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of()
            );
            when(serviceOrderMapper.toResult(savedOrder)).thenReturn(serviceOrderResult);

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    appointmentId,
                    AppointmentStatus.CONFIRMED.name()
            );

            ConfirmAppointmentResult result = useCase.execute(command);

            assertNotNull(result);
            assertNotNull(result.appointmentResult());
            assertNotNull(result.serviceOrderResult());
            assertEquals(appointmentId, result.appointmentResult().id());

            verify(appointmentRepository).save(any(Appointment.class));
            verify(serviceOrderRepository).save(any(ServiceOrder.class));
        }

        @Test
        void shouldSetServiceOrderTotalPrice_asSumOfRequestedServicePrices() {
            UUID appointmentId = UUID.randomUUID();

            Appointment appointment = Appointment.builder()
                    .id(appointmentId)
                    .bikeId(UUID.randomUUID())
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .requestedServices(new ArrayList<>())
                    .build();

            // Requested services to sum: 10.50 + 2.25 = 12.75
            appointment.getRequestedServices().add(RequestedService.builder()
                    .serviceId(UUID.randomUUID())
                    .name("Service A")
                    .price(new BigDecimal("10.50"))
                    .build());
            appointment.getRequestedServices().add(RequestedService.builder()
                    .serviceId(UUID.randomUUID())
                    .name("Service B")
                    .price(new BigDecimal("2.25"))
                    .build());

            when(appointmentRepository.getById(appointmentId)).thenReturn(Optional.of(appointment));
            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0, Appointment.class));

            when(mapper.toResult(any(Appointment.class))).thenReturn(new AppointmentResult(
                    appointmentId,
                    appointment.getBikeId(),
                    appointment.getStoreLocationId(),
                    appointment.getScheduledAt(),
                    AppointmentStatus.CONFIRMED,
                    appointment.getNotes(),
                    List.of()
            ));

            when(serviceOrderRepository.save(serviceOrderCaptor.capture())).thenAnswer(inv -> {
                ServiceOrder toSave = inv.getArgument(0, ServiceOrder.class);
                toSave.setId(UUID.randomUUID());
                return toSave;
            });

            ServiceOrderResult serviceOrderResult = new ServiceOrderResult(
                    UUID.randomUUID(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of()
            );
            when(serviceOrderMapper.toResult(any(ServiceOrder.class))).thenReturn(serviceOrderResult);

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    appointmentId,
                    AppointmentStatus.CONFIRMED.name()
            );

            useCase.execute(command);

            ServiceOrder savedOrder = serviceOrderCaptor.getValue();
            assertEquals(new BigDecimal("12.75"), savedOrder.getTotalPrice());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenAppointmentNotFound() {
            UUID appointmentId = UUID.randomUUID();

            when(appointmentRepository.getById(appointmentId)).thenReturn(Optional.empty());

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    appointmentId,
                    AppointmentStatus.CONFIRMED.name()
            );

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));

            verify(appointmentRepository, never()).save(any(Appointment.class));
            verify(serviceOrderRepository, never()).save(any(ServiceOrder.class));
        }
    }

    @Nested
    class ErrorHandling {

        @Test
        void shouldThrowBadRequest_whenRuntimeExceptionOccurs() {
            UUID appointmentId = UUID.randomUUID();

            Appointment appointment = Appointment.builder()
                    .id(appointmentId)
                    .bikeId(UUID.randomUUID())
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .requestedServices(new ArrayList<>())
                    .build();

            when(appointmentRepository.getById(appointmentId)).thenReturn(Optional.of(appointment));
            when(appointmentRepository.save(any(Appointment.class))).thenThrow(new RuntimeException("db down"));

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    appointmentId,
                    AppointmentStatus.CONFIRMED.name()
            );

            assertThrows(BadRequestException.class, () -> useCase.execute(command));
        }
    }
}
