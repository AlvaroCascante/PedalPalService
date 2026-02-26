package com.quetoquenana.pedalpal.bike.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.application.useCase.UpdateBikeUseCase;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBikeUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    BikeMapper bikeMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    UpdateBikeUseCase useCase;

    @Captor
    ArgumentCaptor<BikeHistoryEvent> historyEventCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateOnlyName_whenOnlyNameIsProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> {
                Bike toSave = inv.getArgument(0, Bike.class);
                toSave.setId(bikeId);
                return toSave;
            });
            when(bikeMapper.toBikeResult(any(Bike.class))).thenAnswer(inv -> TestBikeData.bikeResultFromBike(inv.getArgument(0, Bike.class)));

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_nameOnly(bikeId, ownerId, "New name");

            BikeResult result = useCase.execute(command);

            assertEquals("New name", result.name());
            assertEquals("Old brand", result.brand());
            assertEquals(BikeType.ROAD, result.type());

            verify(bikeRepository, never()).existsBySerialNumber(anyString());
            verify(bikeRepository).findByIdAndOwnerId(eq(bikeId), eq(ownerId));
            verify(bikeRepository).save(any(Bike.class));

            verify(eventPublisher, times(1)).publishEvent(historyEventCaptor.capture());
            BikeHistoryEvent event = historyEventCaptor.getValue();
            assertEquals(bikeId, event.bikeId());
            assertEquals(ownerId, event.performedBy());
            assertEquals(bikeId, event.referenceId());
            assertEquals(BikeHistoryEventType.UPDATED, event.bikeHistoryEventType());
            assertNotNull(event.changes());
            assertEquals(1, event.changes().size());
        }

        @Test
        void shouldUpdateMultipleFields_whenProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(bike));
            when(bikeRepository.existsBySerialNumber("NEW-SN")).thenReturn(false);
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenAnswer(inv -> TestBikeData.bikeResultFromBike(inv.getArgument(0, Bike.class)));

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_allFields(bikeId, ownerId);

            BikeResult result = useCase.execute(command);

            assertEquals("New name", result.name());
            assertEquals(BikeType.MTB, result.type());
            assertEquals("New brand", result.brand());
            assertEquals(999, result.odometerKm());
            assertTrue(result.isPublic());
            assertTrue(result.isExternalSync());

            verify(bikeRepository).existsBySerialNumber("NEW-SN");
            verify(bikeRepository).findByIdAndOwnerId(eq(bikeId), eq(ownerId));
            verify(bikeRepository).save(any(Bike.class));

            verify(eventPublisher, times(1)).publishEvent(any(BikeHistoryEvent.class));
        }
    }

    @Nested
    class GuardAndValidation {

        @Test
        void shouldNotModifyFields_whenNotPresentInCommand() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));
            when(bikeMapper.toBikeResult(any(Bike.class))).thenAnswer(inv -> TestBikeData.bikeResultFromBike(inv.getArgument(0, Bike.class)));

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_noFields(bikeId, ownerId);

            BikeResult result = useCase.execute(command);

            assertEquals("Old name", result.name());
            assertEquals("Old brand", result.brand());
            assertEquals("OLD-SN", result.serialNumber());

            verify(bikeRepository).findByIdAndOwnerId(eq(bikeId), eq(ownerId));
            verify(bikeRepository).save(any(Bike.class));
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowBadRequest_whenBlankFieldProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            Bike bike = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(bike));

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_blankName(bikeId, ownerId);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.update.name.blank", ex.getMessage());

            verify(bikeRepository).findByIdAndOwnerId(eq(bikeId), eq(ownerId));
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenBikeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            when(bikeRepository.findByIdAndOwnerId(eq(bikeId), eq(ownerId))).thenReturn(Optional.empty());

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_nameOnlyNotFound(bikeId, ownerId);

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenOwnerDoesNotMatchAuthenticatedUser() {
            UUID bikeId = UUID.randomUUID();
            UUID otherUser = UUID.randomUUID();

            when(bikeRepository.findByIdAndOwnerId(eq(bikeId), eq(otherUser))).thenReturn(Optional.empty());

            UpdateBikeCommand command = TestBikeData.updateBikeCommand_nameOnlyNotFound(bikeId, otherUser);

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            verify(bikeRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
