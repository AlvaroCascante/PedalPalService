package com.quetoquenana.pedalpal.appointment.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAppointmentUseCaseTest {

    @Mock
    AppointmentMapper mapper;

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    BikeRepository bikeRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CreateAppointmentUseCase useCase;

    @Captor
    ArgumentCaptor<Appointment> appointmentCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldCreateAppointment_withRequestedServicesSnapshots() {
            UUID bikeId = UUID.randomUUID();
            UUID storeLocationId = UUID.randomUUID();
            UUID productId = UUID.randomUUID();

            Appointment mapped = Appointment.builder()
                    .bikeId(bikeId)
                    .storeLocationId(storeLocationId)
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED)
                    .notes("notes")
                    .requestedServices(new ArrayList<>())
                    .build();

            when(mapper.toAppointment(any(CreateAppointmentCommand.class))).thenReturn(mapped);
            when(mapper.toResult(any(Appointment.class))).thenAnswer(inv -> {
                Appointment a = inv.getArgument(0, Appointment.class);
                return AppointmentResult.builder()
                        .id(a.getId())
                        .bikeId(a.getBikeId())
                        .storeLocationId(a.getStoreLocationId())
                        .scheduledAt(a.getScheduledAt())
                        .status(a.getStatus())
                        .notes(a.getNotes())
                        .requestedServices(List.of())
                        .build();
            });

            when(bikeRepository.getById(bikeId)).thenReturn(Optional.of(Bike.builder()
                    .id(bikeId)
                    .ownerId(UUID.randomUUID())
                    .name("Bike")
                    .type(BikeType.ROAD)
                    .status(BikeStatus.ACTIVE)
                    .isPublic(false)
                    .isExternalSync(false)
                    .build()));

            Product product = Product.builder()
                    .id(productId)
                    .name("Chain")
                    .description("Bike chain")
                    .price(new BigDecimal("19.99"))
                    .status(com.quetoquenana.pedalpal.common.domain.model.GeneralStatus.ACTIVE)
                    .build();

            when(productRepository.getById(productId)).thenReturn(Optional.of(product));

            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> {
                Appointment toSave = inv.getArgument(0, Appointment.class);
                toSave.setId(UUID.randomUUID());
                return toSave;
            });

            CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                    .bikeId(bikeId)
                    .storeLocationId(storeLocationId)
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .notes("notes")
                    .requestedServices(List.of(CreateAppointmentCommand.ServiceCommandItem.builder().productId(productId).build()))
                    .authenticatedUserId(UUID.randomUUID())
                    .build();

            AppointmentResult result = useCase.execute(command);

            verify(appointmentRepository, times(1)).save(appointmentCaptor.capture());
            Appointment saved = appointmentCaptor.getValue();

            assertEquals(bikeId, saved.getBikeId());
            assertEquals(storeLocationId, saved.getStoreLocationId());
            assertEquals(AppointmentStatus.REQUESTED, saved.getStatus());
            assertEquals(1, saved.getRequestedServices().size());
            assertEquals("Chain", saved.getRequestedServices().getFirst().getProductNameSnapshot());
            assertEquals(new BigDecimal("19.99"), saved.getRequestedServices().getFirst().getPriceSnapshot());
            assertNotNull(result.id());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();


            when(bikeRepository.getById(bikeId)).thenReturn(Optional.empty());

            CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                    .bikeId(bikeId)
                    .storeLocationId(UUID.randomUUID())
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .requestedServices(List.of())
                    .authenticatedUserId(UUID.randomUUID())
                    .build();

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
        }
    }
}
