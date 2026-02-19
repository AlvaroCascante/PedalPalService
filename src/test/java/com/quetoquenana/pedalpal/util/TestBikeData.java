package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;

import java.util.Collections;
import java.util.UUID;

/**
 * Test data factory methods for bike-related tests.
 */
public final class TestBikeData {

    private TestBikeData() {}

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

    public static BikeResult bikeResult(UUID bikeId) {
        return BikeResult.builder()
                .id(bikeId)
                .name("My bike")
                .type("ROAD")
                .status("ACTIVE")
                .isPublic(false)
                .isExternalSync(false)
                .brand("Brand")
                .model("Model")
                .year(2020)
                .serialNumber(null)
                .notes(null)
                .odometerKm(0)
                .usageTimeMinutes(0)
                .components(Collections.emptySet())
                .build();
    }

    public static BikeResult bikeResultUpdated(UUID bikeId) {
        return BikeResult.builder()
                .id(bikeId)
                .name("New name")
                .type("ROAD")
                .status("ACTIVE")
                .isPublic(true)
                .isExternalSync(false)
                .brand("Brand")
                .model("Model")
                .year(2020)
                .serialNumber(null)
                .notes(null)
                .odometerKm(0)
                .usageTimeMinutes(0)
                .components(Collections.emptySet())
                .build();
    }

    public static BikeResult bikeResultWithStatus(UUID bikeId, String status) {
        return BikeResult.builder()
                .id(bikeId)
                .name("New name")
                .type("ROAD")
                .status(status)
                .isPublic(true)
                .isExternalSync(false)
                .brand("Brand")
                .model("Model")
                .year(2020)
                .serialNumber(null)
                .notes(null)
                .odometerKm(0)
                .usageTimeMinutes(0)
                .components(Collections.emptySet())
                .build();
    }

    public static BikeResult bikeResultQuery(UUID bikeId) {
        return BikeResult.builder()
                .id(bikeId)
                .name("My bike")
                .type("ROAD")
                .status("ACTIVE")
                .isPublic(false)
                .isExternalSync(false)
                .brand("Brand")
                .model("Model")
                .year(2020)
                .serialNumber(null)
                .notes(null)
                .odometerKm(0)
                .usageTimeMinutes(0)
                .components(Collections.emptySet())
                .build();
    }

    // NOTE: older CreateBikeResult/UpdateBikeResult factories keep existing unit tests working.
}
