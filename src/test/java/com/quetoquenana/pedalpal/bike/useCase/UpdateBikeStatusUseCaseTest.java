package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeStatusCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.useCase.UpdateBikeStatusUseCase;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.util.TestBikeData;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBikeStatusUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    BikeMapper bikeMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    UpdateBikeStatusUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateBikeStatus_whenValidStatusProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.INACTIVE);

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultWithStatus(bikeId, "ACTIVE"));

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, "ACTIVE");

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.ACTIVE, bikeCaptor.getValue().getStatus());
            assertEquals("ACTIVE", result.status());
            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(bikeId, event.referenceId());
            assertEquals(BikeHistoryEventType.STATUS_CHANGED, event.bikeHistoryEventType());
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

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, "   ");

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.update.status.blank", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldNotUpdateStatus_whenStatusIsNull() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.INACTIVE);

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultWithStatus(bikeId, "INACTIVE"));

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, null);

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.INACTIVE, bikeCaptor.getValue().getStatus());
            assertEquals("INACTIVE", result.status());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldSetUnknown_whenStatusIsNotRecognized() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = TestBikeData.existingBike(bikeId, ownerId);
            bike.setStatus(BikeStatus.ACTIVE);

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenReturn(TestBikeData.bikeResultWithStatus(bikeId, "UNKNOWN"));

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, "NOT_A_STATUS");

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.UNKNOWN, bikeCaptor.getValue().getStatus());
            assertEquals("UNKNOWN", result.status());
            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(bikeId, event.referenceId());
            assertEquals(BikeHistoryEventType.STATUS_CHANGED, event.bikeHistoryEventType());
            assertNotNull(event.changes());
            assertEquals(1, event.changes().size());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExistForOwner() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.empty());

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, "ACTIVE");

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
