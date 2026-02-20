package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeComponentStatusCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeComponentStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBikeComponentStatusUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @InjectMocks
    UpdateBikeComponentStatusUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

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

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();

            assertNotNull(saved.getComponents());
            BikeComponent savedComponent = saved.getComponents().stream().findFirst().orElseThrow();
            assertEquals(BikeComponentStatus.INACTIVE, savedComponent.getStatus());

            assertEquals(bikeId, result.id());
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
        }
    }
}

