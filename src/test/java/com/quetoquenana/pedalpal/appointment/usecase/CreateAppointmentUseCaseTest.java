package com.quetoquenana.pedalpal.appointment.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.RequestedServiceCommand;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.model.ServiceType;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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

    @Mock
    ProductPackageRepository productPackageRepository;

    @Mock
    AuthenticatedUserPort authenticatedUserPort;

    CreateAppointmentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateAppointmentUseCase(
                mapper,
                appointmentRepository,
                authenticatedUserPort,
                bikeRepository,
                productRepository,
                productPackageRepository
        );
    }

    @Captor
    ArgumentCaptor<Appointment> appointmentCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldCreateAppointment_withRequestedServicesSnapshots() {
            UUID bikeId = UUID.randomUUID();
            UUID storeLocationId = UUID.randomUUID();
            UUID productId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            mockAuthenticatedUser(ownerId);

            Appointment mapped = Appointment.builder()
                    .bikeId(bikeId)
                    .storeLocationId(storeLocationId)
                    .scheduledAt(Instant.parse("2026-02-25T10:00:00Z"))
                    .status(AppointmentStatus.REQUESTED) // Important: mapper doesn't set it; use case must default it.
                    .notes("notes")
                    .requestedServices(new ArrayList<>())
                    .build();

            when(mapper.toModel(any(CreateAppointmentCommand.class))).thenReturn(mapped);
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

            when(bikeRepository.findByIdAndOwnerId(eq(bikeId), eq(ownerId)))
                    .thenReturn(Optional.of(Bike.builder()
                            .id(bikeId)
                            .ownerId(ownerId)
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
                    .status(GeneralStatus.ACTIVE)
                    .build();

            when(productRepository.getByIdAndStatus(eq(productId), eq(GeneralStatus.ACTIVE)))
                    .thenReturn(Optional.of(product));

            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> {
                Appointment toSave = inv.getArgument(0, Appointment.class);
                toSave.setId(UUID.randomUUID());
                return toSave;
            });

            // serviceId refers to Product id in snapshotting logic
            CreateAppointmentCommand command = new CreateAppointmentCommand(
                    bikeId,
                    storeLocationId,
                    Instant.parse("2026-02-25T10:00:00Z"),
                    "notes",
                    List.of(new RequestedServiceCommand(productId, ServiceType.PRODUCT))
            );

            AppointmentResult result = useCase.execute(command);

            verify(appointmentRepository, times(1)).save(appointmentCaptor.capture());
            Appointment saved = appointmentCaptor.getValue();

            assertEquals(bikeId, saved.getBikeId());
            assertEquals(storeLocationId, saved.getStoreLocationId());
            assertEquals(AppointmentStatus.REQUESTED, saved.getStatus());
            assertEquals(1, saved.getRequestedServices().size());
            assertEquals("Chain", saved.getRequestedServices().getFirst().getName());
            assertEquals(new BigDecimal("19.99"), saved.getRequestedServices().getFirst().getPrice());
            assertNotNull(result.id());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            mockAuthenticatedUser(ownerId);

            when(bikeRepository.findByIdAndOwnerId(eq(bikeId), eq(ownerId))).thenReturn(Optional.empty());

            CreateAppointmentCommand command = new CreateAppointmentCommand(
                    bikeId,
                    UUID.randomUUID(),
                    Instant.parse("2026-02-25T10:00:00Z"),
                    null,
                    List.of(new RequestedServiceCommand(UUID.randomUUID(), ServiceType.PRODUCT))
            );

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));

            verify(appointmentRepository, never()).save(any(Appointment.class));
            verify(mapper, never()).toModel(any(CreateAppointmentCommand.class));
        }
    }

    private void mockAuthenticatedUser(UUID ownerId) {
        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(ownerId, "test-user", "Test User", UserType.CUSTOMER)));
    }
}
