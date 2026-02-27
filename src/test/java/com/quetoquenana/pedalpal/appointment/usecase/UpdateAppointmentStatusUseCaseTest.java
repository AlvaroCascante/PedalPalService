package com.quetoquenana.pedalpal.appointment.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentStatusUseCaseTest {

    @Mock
    AppointmentMapper mapper;

    @Mock
    AppointmentRepository appointmentRepository;

    @InjectMocks
    UpdateAppointmentStatusUseCase useCase;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateStatus_andPersist() {
            UUID id = UUID.randomUUID();

            Appointment appointment = Appointment.builder()
                    .id(id)
                    .bikeId(UUID.randomUUID())
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .requestedServices(new ArrayList<>())
                    .build();

            when(appointmentRepository.getById(id)).thenReturn(Optional.of(appointment));
            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0, Appointment.class));
            when(mapper.toResult(any(Appointment.class))).thenAnswer(inv -> {
                Appointment a = inv.getArgument(0, Appointment.class);
                return new AppointmentResult(
                        a.getId(),
                        a.getBikeId(),
                        a.getStoreLocationId(),
                        a.getScheduledAt(),
                        a.getStatus(),
                        a.getNotes(),
                        List.of()
                );
            });

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    id,
                    AppointmentStatus.CONFIRMED.name()
            );

            AppointmentResult result = useCase.execute(command);

            verify(appointmentRepository).save(any(Appointment.class));
            assertEquals(AppointmentStatus.CONFIRMED, result.status());
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowBadRequest_whenStatusIsBlank() {
            UUID id = UUID.randomUUID();

            Appointment appointment = Appointment.builder()
                    .id(id)
                    .bikeId(UUID.randomUUID())
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .requestedServices(new ArrayList<>())
                    .build();

            when(appointmentRepository.getById(id)).thenReturn(Optional.of(appointment));

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    id,
                    "   "
            );

            assertThrows(BadRequestException.class, () -> useCase.execute(command));
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenAppointmentNotFound() {
            UUID id = UUID.randomUUID();

            when(appointmentRepository.getById(id)).thenReturn(Optional.empty());

            UpdateAppointmentStatusCommand command = new UpdateAppointmentStatusCommand(
                    id,
                    AppointmentStatus.CONFIRMED.name()
            );

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
        }
    }
}
