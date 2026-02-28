package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.bike.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;

import java.util.Collections;
import java.util.Set;
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
        return new CreateBikeCommand(
                ownerId,
                "My bike",
                "ROAD",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false
        );
    }

    public static CreateBikeCommand createBikeCommand_withSerial(UUID ownerId, String serialNumber) {
        return new CreateBikeCommand(
                ownerId,
                "Canyon Ultimate",
                "ROAD",
                "Canyon",
                "Ultimate",
                2022,
                serialNumber,
                "Some notes",
                1234,
                5678,
                true,
                true
        );
    }

    public static CreateBikeCommand createBikeCommand_duplicateSerial() {
        return new CreateBikeCommand(
                UUID.randomUUID(),
                "My bike",
                "ROAD",
                null,
                null,
                null,
                "SN-123",
                null,
                null,
                null,
                false,
                false
        );
    }

    public static CreateBikeCommand createBikeCommand_basicWithSerial(UUID ownerId, String serialNumber) {
        return new CreateBikeCommand(
                ownerId,
                "Bike",
                "ROAD",
                null,
                null,
                null,
                serialNumber,
                null,
                null,
                null,
                true,
                false
        );
    }

    public static CreateBikeCommand createBikeCommand_basicNoSerial(UUID ownerId) {
        return new CreateBikeCommand(
                ownerId,
                "Bike",
                "ROAD",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                false
        );
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
        return bikeResult(
                bikeId,
                "My bike",
                BikeType.ROAD,
                BikeStatus.ACTIVE,
                false,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                0,
                0,
                Collections.emptySet()
        );
    }

    public static BikeResult bikeResultUpdated(UUID bikeId) {
        return bikeResult(
                bikeId,
                "New name",
                BikeType.ROAD,
                BikeStatus.ACTIVE,
                true,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                0,
                0,
                Collections.emptySet()
        );
    }

    public static BikeResult bikeResultWithStatus(UUID bikeId, String status) {
        return bikeResult(
                bikeId,
                "New name",
                BikeType.ROAD,
                BikeStatus.from(status),
                true,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                0,
                0,
                Collections.emptySet()
        );
    }

    public static BikeResult bikeResultQuery(UUID bikeId) {
        return bikeResult(
                bikeId,
                "My bike",
                BikeType.ROAD,
                BikeStatus.ACTIVE,
                false,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                0,
                0,
                Collections.emptySet()
        );
    }

    public static BikeResult bikeResultWithComponentStatus(UUID bikeId, String componentStatus) {
        BikeComponentResult component = new BikeComponentResult(
                UUID.randomUUID(),
                SystemCode.builder()
                        .id(UUID.randomUUID())
                        .category("COMPONENT_TYPE")
                        .code("CHAIN")
                        .label("Chain")
                        .status(GeneralStatus.ACTIVE)
                        .position(1)
                        .build(),
                "Chain",
                BikeComponentStatus.from(componentStatus),
                "Brand",
                "Model",
                null,
                0,
                0
        );
        return new BikeResult(
                bikeId,
                "Bike",
                BikeType.ROAD,
                BikeStatus.ACTIVE,
                false,
                false,
                "Brand",
                "Model",
                2020,
                null,
                null,
                0,
                0,
                Set.of(component)
        );
    }

    public static BikeResult bikeResultFromBike(Bike bike) {
        return bikeResult(
                bike.getId(),
                bike.getName(),
                bike.getType() == null ? null : bike.getType(),
                bike.getStatus() == null ? null : bike.getStatus(),
                bike.isPublic(),
                bike.isExternalSync(),
                bike.getBrand(),
                bike.getModel(),
                bike.getYear(),
                bike.getSerialNumber(),
                bike.getNotes(),
                bike.getOdometerKm() == null ? 0 : bike.getOdometerKm(),
                bike.getUsageTimeMinutes() == null ? 0 : bike.getUsageTimeMinutes(),
                bike.getComponents() == null
                        ? Collections.emptySet()
                        : bike.getComponents().stream()
                        .map(TestBikeData::toComponentResult)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    public static BikeResult bikeResultWithOneComponent(UUID bikeId) {
        return bikeResultWithComponentStatus(bikeId, "ACTIVE");
    }

    private static BikeComponentResult toComponentResult(BikeComponent component) {
        return new BikeComponentResult(
                component.getId(),
                component.getComponentType(),
                component.getName(),
                component.getStatus(),
                component.getBrand(),
                component.getModel(),
                component.getNotes(),
                component.getOdometerKm() == null ? 0 : component.getOdometerKm(),
                component.getUsageTimeMinutes() == null ? 0 : component.getUsageTimeMinutes()
        );
    }

    private static BikeResult bikeResult(
            UUID id,
            String name,
            BikeType type,
            BikeStatus status,
            boolean isPublic,
            boolean isExternalSync,
            String brand,
            String model,
            Integer year,
            String serialNumber,
            String notes,
            int odometerKm,
            int usageTimeMinutes,
            Set<BikeComponentResult> components
    ) {
        return new BikeResult(
                id,
                name,
                type,
                status,
                isPublic,
                isExternalSync,
                brand,
                model,
                year,
                serialNumber,
                notes,
                odometerKm,
                usageTimeMinutes,
                components
        );
    }

    // NOTE: older CreateBikeResult/UpdateBikeResult factories keep existing unit tests working.
}
