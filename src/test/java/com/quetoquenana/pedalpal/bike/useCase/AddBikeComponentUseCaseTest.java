package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.useCase.AddBikeComponentUseCase;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import com.quetoquenana.pedalpal.common.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.util.TestBikeData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.context.ApplicationEventPublisher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBikeComponentUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    SystemCodeRepository systemCodeRepository;

    @Mock
    BikeMapper bikeMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    AddBikeComponentUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldAddComponentAndPersistBike_whenCommandIsValid() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .brand("Shimano")
                    .model("HG")
                    .notes("New chain")
                    .odometerKm(10)
                    .usageTimeMinutes(20)
                    .build();

            SystemCode componentType = SystemCode.builder()
                    .id(UUID.randomUUID())
                    .category(COMPONENT_TYPE)
                    .code("CHAIN")
                    .label("Chain")
                    .isActive(true)
                    .position(1)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(systemCodeRepository.findByCategoryAndCode(eq(COMPONENT_TYPE), eq("CHAIN")))
                    .thenReturn(Optional.of(componentType));

            BikeComponent mappedComponent = BikeComponent.builder()
                    .id(UUID.randomUUID())
                    .componentType(componentType)
                    .name(command.name())
                    .status(BikeComponentStatus.ACTIVE)
                    .brand(command.brand())
                    .model(command.model())
                    .notes(command.notes())
                    .odometerKm(command.odometerKm())
                    .usageTimeMinutes(command.usageTimeMinutes())
                    .build();

            when(bikeMapper.toBikeComponent(eq(command), eq(componentType))).thenReturn(mappedComponent);
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultWithOneComponent(bikeId));

            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            assertEquals(bikeId, saved.getId());
            assertEquals(ownerId, saved.getOwnerId());
            assertNotNull(saved.getComponents());
            assertEquals(1, saved.getComponents().size());

            assertEquals(bikeId, result.id());
            assertEquals(BikeType.ROAD, result.type());
            assertEquals(1, result.components().size());

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(mappedComponent.getId(), event.referenceId());
            assertEquals(BikeHistoryEventType.COMPONENT_ADDED, event.bikeHistoryEventType());
        }
    }

    @Nested
    class NotFoundScenarios {

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExistForOwner() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.empty());

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.not.found", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(anyString(), anyString());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenComponentTypeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .authenticatedUserId(ownerId)
                    .type("NOT_A_TYPE")
                    .name("Some component")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(systemCodeRepository.findByCategoryAndCode(eq(COMPONENT_TYPE), eq("NOT_A_TYPE")))
                    .thenReturn(Optional.empty());

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.component.type.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }

    @Nested
    class ValidationAndGuards {

        @Test
        void shouldThrowBadRequest_whenBikeIsNotActive() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.INACTIVE);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.update.no.active", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(anyString(), anyString());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowBadRequest_whenBikeStatusIsNull() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(null);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.update.no.active", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(anyString(), anyString());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
