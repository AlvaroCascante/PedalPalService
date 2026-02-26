package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.useCase.CreateBikeUseCase;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.util.TestBikeData;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBikeUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    BikeMapper bikeMapper;

    @InjectMocks
    CreateBikeUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPathScenarios {

        @Test
        void shouldCreateBikeSuccessfully_whenAllDataIsValid_andSerialIsNull() {
            UUID ownerId = UUID.randomUUID();

            CreateBikeCommand command = TestBikeData.createBikeCommand_minimal(ownerId);

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> {
                Bike toSave = inv.getArgument(0, Bike.class);
                toSave.setId(UUID.randomUUID());
                return toSave;
            });

            Bike mapped = Bike.builder()
                    .ownerId(ownerId)
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(command.serialNumber())
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();

            when(bikeMapper.toBike(command)).thenReturn(mapped);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenAnswer(inv -> {
                Bike b = inv.getArgument(0, Bike.class);
                return new BikeResult(
                        b.getId(),
                        b.getName(),
                        b.getType(),
                        b.getStatus(),
                        b.isPublic(),
                        b.isExternalSync(),
                        b.getBrand(),
                        b.getModel(),
                        b.getYear(),
                        b.getSerialNumber(),
                        b.getNotes(),
                        b.getOdometerKm() == null ? 0 : b.getOdometerKm(),
                        b.getUsageTimeMinutes() == null ? 0 : b.getUsageTimeMinutes(),
                        java.util.Set.of()
                );
            });

            BikeResult result = useCase.execute(command);

            verify(bikeRepository, never()).existsBySerialNumber(anyString());
            verify(bikeRepository, times(1)).save(bikeCaptor.capture());

            Bike saved = bikeCaptor.getValue();

            assertNotNull(saved.getId());
            assertEquals(ownerId, saved.getOwnerId());
            assertEquals(BikeType.ROAD, saved.getType());
            assertNull(saved.getSerialNumber());
            assertEquals(BikeStatus.ACTIVE, saved.getStatus());

            assertEquals(saved.getId(), result.id());
            assertEquals(command.name(), result.name());
            assertEquals(BikeType.ROAD, result.type());
            assertNull(result.serialNumber());

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(saved.getId(), event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(saved.getId(), event.referenceId());
            assertEquals(BikeHistoryEventType.CREATED, event.bikeHistoryEventType());
        }

        @Test
        void shouldCreateBikeSuccessfully_withSerialNumber_whenSerialIsUnique() {
            UUID ownerId = UUID.randomUUID();

            CreateBikeCommand command = TestBikeData.createBikeCommand_withSerial(ownerId, "SN-123");

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> {
                Bike toSave = inv.getArgument(0, Bike.class);
                if (toSave.getId() == null) {
                    toSave.setId(UUID.randomUUID());
                }
                return toSave;
            });

            Bike mapped = Bike.builder()
                    .ownerId(ownerId)
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(command.serialNumber())
                    .brand(command.brand())
                    .model(command.model())
                    .year(command.year())
                    .notes(command.notes())
                    .odometerKm(command.odometerKm())
                    .usageTimeMinutes(command.usageTimeMinutes())
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();

            when(bikeMapper.toBike(command)).thenReturn(mapped);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenAnswer(inv -> TestBikeData.bikeResultFromBike(inv.getArgument(0, Bike.class)));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository, times(1)).existsBySerialNumber(command.serialNumber());
            verify(bikeRepository, times(1)).save(bikeCaptor.capture());

            Bike saved = bikeCaptor.getValue();
            assertEquals(ownerId, saved.getOwnerId());
            assertEquals(command.name(), saved.getName());
            assertEquals(BikeType.ROAD, saved.getType());
            assertEquals(BikeStatus.ACTIVE, saved.getStatus());
            assertEquals(command.serialNumber(), saved.getSerialNumber());

            assertEquals(command.isPublic(), saved.isPublic());
            assertEquals(command.isExternalSync(), saved.isExternalSync());
            assertEquals(command.brand(), saved.getBrand());
            assertEquals(command.model(), saved.getModel());
            assertEquals(command.year(), saved.getYear());
            assertEquals(command.notes(), saved.getNotes());
            assertEquals(command.odometerKm(), saved.getOdometerKm());
            assertEquals(command.usageTimeMinutes(), saved.getUsageTimeMinutes());

            assertEquals(BikeType.ROAD, result.type());
            assertEquals(command.serialNumber(), result.serialNumber());

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(event.bikeId(), result.id());
            assertEquals(ownerId, event.performedBy());
            assertEquals(result.id(), event.referenceId());
            assertEquals(BikeHistoryEventType.CREATED, event.bikeHistoryEventType());
        }
    }

    @Nested
    class ValidationFailures_BusinessRules {

        @Test
        void shouldFail_whenSerialNumberAlreadyExists_andNotPersistAnything() {
            CreateBikeCommand command = TestBikeData.createBikeCommand_duplicateSerial();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(true);

            BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(command));
            assertEquals("bike.serial.number.already.exists", ex.getMessage());

            verify(bikeRepository, times(1)).existsBySerialNumber(command.serialNumber());
            verify(bikeRepository, never()).save(any());
        }

        @Test
        void shouldNotCheckSerialUniqueness_whenSerialIsNull_andStillPersist() {
            CreateBikeCommand command = TestBikeData.createBikeCommand_minimal(UUID.randomUUID());

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            Bike mapped = Bike.builder()
                    .ownerId(command.ownerId())
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(null)
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();
            when(bikeMapper.toBike(command)).thenReturn(mapped);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultQuery(UUID.randomUUID()));

            useCase.execute(command);

            verify(bikeRepository, never()).existsBySerialNumber(anyString());
            verify(bikeRepository, times(1)).save(any(Bike.class));
        }

        @Test
        void shouldFail_withBusinessException_whenRepositoryThrowsDataIntegrityViolation() {
            CreateBikeCommand command = TestBikeData.createBikeCommand_duplicateSerial();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenThrow(new DataIntegrityViolationException("dup"));

            Bike mapped = Bike.builder()
                    .ownerId(command.ownerId())
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(command.serialNumber())
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();
            when(bikeMapper.toBike(command)).thenReturn(mapped);

            BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(command));
            assertEquals("bike.creation.failed", ex.getMessage());

            verify(bikeRepository, times(1)).save(any(Bike.class));
        }
    }

    @Nested
    class DomainBehaviorVerification {

        @Test
        void shouldMapCommandToDomainBikeCorrectly_beforeSaving() {
            UUID ownerId = UUID.randomUUID();

            CreateBikeCommand command = TestBikeData.createBikeCommand_basicWithSerial(ownerId, "SN-999");

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            Bike mapped = Bike.builder()
                    .ownerId(ownerId)
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(command.serialNumber())
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();
            when(bikeMapper.toBike(command)).thenReturn(mapped);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultQuery(UUID.randomUUID()));

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            assertEquals(ownerId, saved.getOwnerId());
            assertEquals(BikeType.ROAD, saved.getType());
            assertEquals(command.serialNumber(), saved.getSerialNumber());
            assertEquals(BikeStatus.ACTIVE, saved.getStatus());
            assertEquals(command.isExternalSync(), saved.isExternalSync());
            assertEquals(command.isPublic(), saved.isPublic());
        }

        @Test
        void shouldSetDefaultStatusToActive() {
            CreateBikeCommand command = TestBikeData.createBikeCommand_basicNoSerial(UUID.randomUUID());

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            Bike mapped = Bike.builder()
                    .ownerId(command.ownerId())
                    .name(command.name())
                    .type(BikeType.from(command.type()))
                    .status(BikeStatus.ACTIVE)
                    .serialNumber(command.serialNumber())
                    .isPublic(command.isPublic())
                    .isExternalSync(command.isExternalSync())
                    .build();
            when(bikeMapper.toBike(command)).thenReturn(mapped);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultQuery(UUID.randomUUID()));

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.ACTIVE, bikeCaptor.getValue().getStatus());
        }
    }

    @Nested
    class RepositoryInteractionTests {

        @Test
        void shouldNotCallSave_whenValidationFails_dueToDuplicateSerial() {
            CreateBikeCommand command = TestBikeData.createBikeCommand_duplicateSerial();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(true);

            assertThrows(BusinessException.class, () -> useCase.execute(command));

            verify(bikeRepository, never()).save(any());
        }
    }
}
