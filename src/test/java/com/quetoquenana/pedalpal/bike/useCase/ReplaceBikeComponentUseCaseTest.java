package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.useCase.ReplaceBikeComponentUseCase;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import com.quetoquenana.pedalpal.common.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.util.TestBikeData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;
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
class ReplaceBikeComponentUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    SystemCodeRepository systemCodeRepository;

    @Mock
    BikeMapper bikeMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    ReplaceBikeComponentUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldReplaceComponentAndPersistBike_whenCommandIsValid() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            BikeComponent existing = BikeComponent.builder()
                    .id(componentId)
                    .name("Old chain")
                    .status(BikeComponentStatus.ACTIVE)
                    .build();
            bike.addComponent(existing);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("New chain")
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
            when(bikeMapper.toBikeComponent(eq(command), eq(componentType))).thenReturn(
                    BikeComponent.builder()
                            .id(UUID.randomUUID())
                            .name("New chain")
                            .status(BikeComponentStatus.ACTIVE)
                            .build()
            );
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultUpdated(bikeId));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            assertEquals(bikeId, saved.getId());
            assertEquals(ownerId, saved.getOwnerId());
            assertNotNull(saved.getComponents());
            assertEquals(2, saved.getComponents().size());

            assertEquals(BikeComponentStatus.REPLACED, existing.getStatus());
            assertEquals(bikeId, result.id());

            verify(eventPublisher, times(2)).publishEvent(historyEventCaptor.capture());
            var events = historyEventCaptor.getAllValues();
            assertEquals(2, events.size());
            assertEquals(BikeHistoryEventType.COMPONENT_REPLACED, events.get(0).bikeHistoryEventType());
            assertEquals(BikeHistoryEventType.COMPONENT_ADDED, events.get(1).bikeHistoryEventType());
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
                    .componentId(UUID.randomUUID())
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.empty());

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.not.found", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(any(), any());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenComponentIsNotOnBike() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("Chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.component.not.found", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(any(), any());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenComponentTypeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.ACTIVE);
            bike.addComponent(BikeComponent.builder()
                    .id(componentId)
                    .name("Old")
                    .status(BikeComponentStatus.ACTIVE)
                    .build());

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
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
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.INACTIVE);
            bike.addComponent(BikeComponent.builder().id(componentId).name("Old chain").status(BikeComponentStatus.ACTIVE).build());

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("New chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.update.no.active", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(any(), any());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowBadRequest_whenExistingComponentIsNotActive() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.addComponent(BikeComponent.builder().id(componentId).name("Old chain").status(BikeComponentStatus.INACTIVE).build());

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("New chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.component.update.no.active", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(any(), any());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowBadRequest_whenExistingComponentStatusIsNull() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.addComponent(BikeComponent.builder().id(componentId).name("Old chain").status(null).build());

            AddBikeComponentCommand command = AddBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CHAIN")
                    .name("New chain")
                    .odometerKm(0)
                    .usageTimeMinutes(0)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.component.update.no.active", ex.getMessage());

            verify(systemCodeRepository, never()).findByCategoryAndCode(any(), any());
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
