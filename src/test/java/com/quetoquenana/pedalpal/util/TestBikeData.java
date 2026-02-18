package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.application.command.UpdateBikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;

import java.util.UUID;

/**
 * Test data factory methods for bike-related tests.
 */
public final class TestBikeData {

    private TestBikeData() {}

    public static UpdateBikeResult updateBikeResult(UUID bikeId) {
        return new UpdateBikeResult(
                bikeId,
                "New name",
                "ROAD",
                true,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                BikeStatus.ACTIVE.name(),
                0,
                0
        );
    }

    public static UpdateBikeCommand updateBikeCommand_nameOnly(UUID bikeId, UUID ownerId, String name) {
        return new UpdateBikeCommand(
                bikeId,
                ownerId,
                name,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static Bike existingBike(UUID id, UUID ownerId) {
        return Bike.builder()
                .id(id)
                .ownerId(ownerId)
                .name("Old name")
                .type(BikeType.ROAD)
                .status(BikeStatus.ACTIVE)
                .isPublic(false)
                .isExternalSync(false)
                .brand("Old brand")
                .model("Old model")
                .year(2020)
                .serialNumber("OLD-SN")
                .notes("Old notes")
                .odometerKm(100)
                .usageTimeMinutes(200)
                .build();
    }

    public static CreateBikeCommand createBikeCommand_minimal(UUID ownerId) {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name("My bike")
                .type("ROAD")
                .serialNumber(null)
                .isPublic(false)
                .isExternalSync(false)
                .build();
    }

    public static CreateBikeCommand createBikeCommand_withSerial(UUID ownerId, String serialNumber) {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name("Canyon Ultimate")
                .type("ROAD")
                .serialNumber(serialNumber)
                .isPublic(true)
                .isExternalSync(true)
                .brand("Canyon")
                .model("Ultimate")
                .year(2022)
                .notes("Some notes")
                .odometerKm(1234)
                .usageTimeMinutes(5678)
                .build();
    }

    public static CreateBikeCommand createBikeCommand_duplicateSerial() {
        return CreateBikeCommand.builder()
                .ownerId(UUID.randomUUID())
                .name("My bike")
                .type("ROAD")
                .serialNumber("SN-123")
                .isPublic(false)
                .isExternalSync(false)
                .build();
    }

    public static CreateBikeCommand createBikeCommand_basicWithSerial(UUID ownerId, String serialNumber) {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name("Bike")
                .type("ROAD")
                .serialNumber(serialNumber)
                .isPublic(true)
                .isExternalSync(false)
                .build();
    }

    public static CreateBikeCommand createBikeCommand_basicNoSerial(UUID ownerId) {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name("Bike")
                .type("ROAD")
                .serialNumber(null)
                .isPublic(false)
                .isExternalSync(false)
                .build();
    }

    public static UpdateBikeCommand updateBikeCommand_allFields(UUID bikeId, UUID ownerId) {
        return new UpdateBikeCommand(
                bikeId,
                ownerId,
                "New name",
                "MTB",
                "New brand",
                "New model",
                2022,
                "NEW-SN",
                "New notes",
                999,
                888,
                true,
                true
        );
    }

    public static UpdateBikeCommand updateBikeCommand_noFields(UUID bikeId, UUID ownerId) {
        return new UpdateBikeCommand(
                bikeId,
                ownerId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static UpdateBikeCommand updateBikeCommand_blankName(UUID bikeId, UUID ownerId) {
        return updateBikeCommand_nameOnly(bikeId, ownerId, "   ");
    }

    public static UpdateBikeCommand updateBikeCommand_nameOnlyNotFound(UUID bikeId, UUID ownerId) {
        return updateBikeCommand_nameOnly(bikeId, ownerId, "New name");
    }

    public static CreateBikeResult createBikeResult(UUID bikeId, UUID ownerId) {
        return CreateBikeResult.builder()
                .id(bikeId)
                .ownerId(ownerId)
                .name("My bike")
                .type("ROAD")
                .isPublic(false)
                .isExternalSync(false)
                .brand("Brand")
                .model("Model")
                .year(2020)
                .serialNumber(null)
                .notes(null)
                .odometerKm(0)
                .usageTimeMinutes(0)
                .build();
    }

    public static com.quetoquenana.pedalpal.application.command.UpdateBikeResult updateBikeResultWithStatus(UUID bikeId, String status) {
        return new com.quetoquenana.pedalpal.application.command.UpdateBikeResult(
                bikeId,
                "New name",
                "ROAD",
                true,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                status,
                0,
                0
        );
    }
}
