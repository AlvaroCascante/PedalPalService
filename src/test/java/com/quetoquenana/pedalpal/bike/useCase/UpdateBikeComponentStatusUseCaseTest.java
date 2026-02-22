package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeComponentStatusCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.useCase.UpdateBikeComponentStatusUseCase;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBikeComponentStatusUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    BikeMapper bikeMapper;

    @InjectMocks
    UpdateBikeComponentStatusUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateComponentStatusAndPersistBike_whenCommandIsValid() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            BikeComponent component = BikeComponent.builder()
                    .id(componentId)
                    .name("Chain")
                    .status(BikeComponentStatus.ACTIVE)
                    .build();
            bike.addComponent(component);

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .status("INACTIVE")
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultUpdated(bikeId));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            assertNotNull(saved.getComponents());
            BikeComponent savedComponent = saved.getComponents().stream().findFirst().orElseThrow();
            assertEquals(BikeComponentStatus.INACTIVE, savedComponent.getStatus());

            assertEquals(bikeId, result.id());

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(componentId, event.referenceId());
            assertEquals(BikeHistoryEventType.COMPONENT_STATUS_CHANGED, event.bikeHistoryEventType());
            assertNotNull(event.changes());
            assertEquals(1, event.changes().size());
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowBadRequest_whenStatusIsBlank() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.addComponent(BikeComponent.builder().id(componentId).name("Chain").build());

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .status("   ")
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.component.update.status.blank", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldNotUpdateComponentStatus_whenStatusIsNull() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            BikeComponent component = BikeComponent.builder()
                    .id(componentId)
                    .name("Chain")
                    .status(BikeComponentStatus.ACTIVE)
                    .build();
            bike.addComponent(component);

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .status(null)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultUpdated(bikeId));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            BikeComponent savedComponent = saved.getComponents().stream()
                    .filter(c -> c.getId().equals(componentId))
                    .findFirst()
                    .orElseThrow();

            assertEquals(BikeComponentStatus.ACTIVE, savedComponent.getStatus());
            assertEquals(bikeId, result.id());

            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldSetUnknown_whenComponentStatusIsNotRecognized() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            BikeComponent component = BikeComponent.builder()
                    .id(componentId)
                    .name("Chain")
                    .status(BikeComponentStatus.ACTIVE)
                    .build();
            bike.addComponent(component);

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .status("NOT_A_STATUS")
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultUpdated(bikeId));

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            BikeComponent savedComponent = saved.getComponents().stream()
                    .filter(c -> c.getId().equals(componentId))
                    .findFirst()
                    .orElseThrow();

            assertEquals(BikeComponentStatus.UNKNOWN, savedComponent.getStatus());
            assertEquals(bikeId, result.id());

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(componentId, event.referenceId());
            assertEquals(BikeHistoryEventType.COMPONENT_STATUS_CHANGED, event.bikeHistoryEventType());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExistForOwner() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(UUID.randomUUID())
                    .authenticatedUserId(ownerId)
                    .status("ACTIVE")
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.empty());

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenComponentIsNotOnBike() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            UpdateBikeComponentStatusCommand command = UpdateBikeComponentStatusCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .status("ACTIVE")
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.component.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
