package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeComponentCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.domain.model.SystemCode;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.domain.repository.SystemCodeRepository;
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

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBikeComponentUseCaseTest {

    @Mock
    BikeRepository bikeRepository;

    @Mock
    SystemCodeRepository systemCodeRepository;

    @InjectMocks
    UpdateBikeComponentUseCase useCase;

    @Captor
    ArgumentCaptor<Bike> bikeCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateOnlyName_whenOnlyNameProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Old chain");

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .name("New chain")
                    .build();

            BikeResult result = useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            Bike saved = bikeCaptor.getValue();
            BikeComponent updated = saved.getComponents().iterator().next();

            assertEquals(componentId, updated.getId());
            assertEquals("New chain", updated.getName());
            assertEquals("CHAIN", updated.getComponentType().getCode());
            assertEquals(bikeId, result.id());
            assertEquals(1, result.components().size());
        }

        @Test
        void shouldUpdateType_whenTypeProvidedAndExists() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");

            SystemCode cassetteType = SystemCode.builder()
                    .id(UUID.randomUUID())
                    .category(COMPONENT_TYPE)
                    .code("CASSETTE")
                    .label("Cassette")
                    .isActive(true)
                    .position(2)
                    .build();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, "CASSETTE"))
                    .thenReturn(Optional.of(cassetteType));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("CASSETTE")
                    .build();

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            BikeComponent updated = bikeCaptor.getValue().getComponents().iterator().next();
            assertEquals("CASSETTE", updated.getComponentType().getCode());
        }

        @Test
        void shouldUpdateMultipleFields_whenProvided() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .name("New name")
                    .brand("New brand")
                    .model("New model")
                    .notes("New notes")
                    .odometerKm(111)
                    .usageTimeMinutes(222)
                    .build();

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            BikeComponent updated = bikeCaptor.getValue().getComponents().iterator().next();

            assertEquals("New name", updated.getName());
            assertEquals("New brand", updated.getBrand());
            assertEquals("New model", updated.getModel());
            assertEquals("New notes", updated.getNotes());
            assertEquals(111, updated.getOdometerKm());
            assertEquals(222, updated.getUsageTimeMinutes());
        }
    }

    @Nested
    class ValidationAndGuards {

        @Test
        void shouldNotModifyFields_whenNotPresentInCommand() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");
            BikeComponent original = bike.getComponents().iterator().next();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(bikeRepository.save(any(Bike.class))).thenAnswer(inv -> inv.getArgument(0, Bike.class));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .build();

            useCase.execute(command);

            verify(bikeRepository).save(bikeCaptor.capture());
            BikeComponent updated = bikeCaptor.getValue().getComponents().iterator().next();

            assertEquals(original.getName(), updated.getName());
            assertEquals(original.getComponentType().getCode(), updated.getComponentType().getCode());
        }

        @Test
        void shouldThrowBadRequest_whenNameIsBlank() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .name("   ")
                    .build();

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.component.update.name.blank", ex.getMessage());

            verify(bikeRepository, never()).save(any());
        }

        @Test
        void shouldThrowBadRequest_whenTypeIsBlank() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("   ")
                    .build();

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("bike.component.update.type.blank", ex.getMessage());

            verify(bikeRepository, never()).save(any());
            verify(systemCodeRepository, never()).findByCategoryAndCode(anyString(), anyString());
        }
    }

    @Nested
    class NotFound {

        @Test
        void shouldThrowRecordNotFound_whenBikeNotFoundForOwner() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.empty());

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .name("New name")
                    .build();

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenComponentNotFoundOnBike() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bikeWithoutComponent = TestBikeData.existingBike(bikeId, ownerId);

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bikeWithoutComponent));

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .name("New name")
                    .build();

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.component.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
        }

        @Test
        void shouldThrowRecordNotFound_whenTypeDoesNotExist() {
            UUID bikeId = UUID.randomUUID();
            UUID componentId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Bike bike = bikeWithSingleComponent(bikeId, ownerId, componentId, "CHAIN", "Chain");

            when(bikeRepository.findByIdAndOwnerId(bikeId, ownerId)).thenReturn(Optional.of(bike));
            when(systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, "NOT_A_TYPE"))
                    .thenReturn(Optional.empty());

            UpdateBikeComponentCommand command = UpdateBikeComponentCommand.builder()
                    .bikeId(bikeId)
                    .componentId(componentId)
                    .authenticatedUserId(ownerId)
                    .type("NOT_A_TYPE")
                    .build();

            RecordNotFoundException ex = assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            assertEquals("bike.component.type.not.found", ex.getMessage());

            verify(bikeRepository, never()).save(any());
        }
    }

    private static Bike bikeWithSingleComponent(UUID bikeId, UUID ownerId, UUID componentId, String typeCode, String componentName) {
        SystemCode type = SystemCode.builder()
                .id(UUID.randomUUID())
                .category(COMPONENT_TYPE)
                .code(typeCode)
                .label(typeCode)
                .isActive(true)
                .position(1)
                .build();

        BikeComponent component = BikeComponent.builder()
                .id(componentId)
                .componentType(type)
                .name(componentName)
                .brand("Old brand")
                .model("Old model")
                .notes("Old notes")
                .odometerKm(10)
                .usageTimeMinutes(20)
                .build();

        Bike bike = TestBikeData.existingBike(bikeId, ownerId);
        bike.addComponent(component);
        return bike;
    }
}
