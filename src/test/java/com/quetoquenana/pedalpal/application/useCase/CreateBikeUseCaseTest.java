package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBikeUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @InjectMocks
    CreateBikeUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Nested
    class HappyPathScenarios {

        @Test
        void shouldCreateBikeSuccessfully_whenAllDataIsValid_andSerialIsNull() {
            UUID ownerId = UUID.randomUUID();

            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(ownerId)
                    .name("My bike")
                    .type("ROAD")
                    .serialNumber(null)
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            CreateBikeResult result = useCase.execute(command);

            verify(bikeRepository, never()).existsBySerialNumber(anyString());
            verify(bikeRepository, times(1)).save(bikeCaptor.capture());

            Bike saved = bikeCaptor.getValue();
            assertNotNull(saved.getId());
            assertEquals(ownerId, saved.getOwnerId());
            assertEquals(BikeType.ROAD, saved.getType());
            assertNull(saved.getSerialNumber());
            assertEquals(BikeStatus.ACTIVE, saved.getStatus());

            assertEquals(saved.getId(), result.id());
            assertEquals(ownerId, result.ownerId());
            assertEquals(command.name(), result.name());
            assertEquals("ROAD", result.type());
            assertNull(result.serialNumber());
        }

        @Test
        void shouldCreateBikeSuccessfully_withSerialNumber_whenSerialIsUnique() {
            UUID ownerId = UUID.randomUUID();

            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(ownerId)
                    .name("Canyon Ultimate")
                    .type("ROAD")
                    .serialNumber("SN-123")
                    .isPublic(true)
                    .isExternalSync(true)
                    .brand("Canyon")
                    .model("Ultimate")
                    .year(2022)
                    .notes("Some notes")
                    .odometerKm(1234)
                    .usageTimeMinutes(5678)
                    .build();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            CreateBikeResult result = useCase.execute(command);

            verify(bikeRepository, times(1)).existsBySerialNumber(command.serialNumber());
            verify(bikeRepository, times(1)).save(bikeCaptor.capture());

            Bike saved = bikeCaptor.getValue();
            assertNotNull(saved.getId(), "Use case should assign an id");
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

            assertEquals(saved.getId(), result.id());
            assertEquals(ownerId, result.ownerId());
            assertEquals("ROAD", result.type());
            assertEquals(command.serialNumber(), result.serialNumber());
        }
    }

    @Nested
    class ValidationFailures_BusinessRules {

        @Test
        void shouldFail_whenSerialNumberAlreadyExists_andNotPersistAnything() {
            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(UUID.randomUUID())
                    .name("My bike")
                    .type("ROAD")
                    .serialNumber("SN-123")
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(true);

            BusinessException ex = assertThrows(BusinessException.class, () -> useCase.execute(command));
            assertEquals("bike.serial.number.already.exists", ex.getMessage());

            verify(bikeRepository, times(1)).existsBySerialNumber(command.serialNumber());
            verify(bikeRepository, never()).save(any());
        }

        @Test
        void shouldNotCheckSerialUniqueness_whenSerialIsNull_andStillPersist() {
            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(UUID.randomUUID())
                    .name("My bike")
                    .type("ROAD")
                    .serialNumber(null)
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            useCase.execute(command);

            verify(bikeRepository, never()).existsBySerialNumber(anyString());
            verify(bikeRepository, times(1)).save(any(Bike.class));
        }

        @Test
        void shouldFail_withBusinessException_whenRepositoryThrowsDataIntegrityViolation() {
            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(UUID.randomUUID())
                    .name("My bike")
                    .type("ROAD")
                    .serialNumber("SN-123")
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenThrow(new DataIntegrityViolationException("dup"));

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

            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(ownerId)
                    .name("Bike")
                    .type("ROAD")
                    .serialNumber("SN-999")
                    .isPublic(true)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

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
            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(UUID.randomUUID())
                    .name("Bike")
                    .type("ROAD")
                    .serialNumber(null)
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.ACTIVE, bikeCaptor.getValue().getStatus());
        }
    }

    @Nested
    class RepositoryInteractionTests {

        @Test
        void shouldNotCallSave_whenValidationFails_dueToDuplicateSerial() {
            CreateBikeCommand command = CreateBikeCommand.builder()
                    .ownerId(UUID.randomUUID())
                    .name("Bike")
                    .type("ROAD")
                    .serialNumber("SN-123")
                    .isPublic(false)
                    .isExternalSync(false)
                    .build();

            when(bikeRepository.existsBySerialNumber(command.serialNumber())).thenReturn(true);

            assertThrows(BusinessException.class, () -> useCase.execute(command));

            verify(bikeRepository, never()).save(any());
        }
    }

    // NOTE: Bike type validation happens via BikeType.valueOf(command.type()) (IllegalArgumentException if invalid).
    // If you want a BusinessException for invalid type, implement it in the use case and add tests.
}
