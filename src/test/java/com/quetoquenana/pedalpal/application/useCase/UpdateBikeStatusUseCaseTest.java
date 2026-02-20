package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeStatusCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.util.TestBikeData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBikeStatusUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @InjectMocks
    UpdateBikeStatusUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

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

            UpdateBikeStatusCommand command = new UpdateBikeStatusCommand(bikeId, ownerId, "ACTIVE");

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            assertEquals(BikeStatus.ACTIVE, bikeCaptor.getValue().getStatus());
            assertEquals("ACTIVE", result.status());
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
        }
    }
}

