package com.quetoquenana.pedalpal.application.mapper;

import com.quetoquenana.pedalpal.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeComponentStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;

import java.util.stream.Collectors;

public class BikeMapper {

    public static Bike toBike(CreateBikeCommand command) {
        return Bike.builder()
                .ownerId(command.ownerId())
                .name(command.name())
                .type(BikeType.from(command.type()))
                .status(BikeStatus.ACTIVE)
                .isPublic(command.isPublic())
                .isExternalSync(command.isExternalSync())
                .brand(command.brand())
                .model(command.model())
                .year(command.year())
                .serialNumber(command.serialNumber())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();
    }

    public static BikeComponent toBikeComponent(AddBikeComponentCommand command) {
        // Type is determined by the component type (database), so it is not set here
        return BikeComponent.builder()
                .name(command.name())
                .status(BikeComponentStatus.ACTIVE)
                .brand(command.brand())
                .model(command.model())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();
    }

    public static BikeResult toBikeResult(Bike model) {
        return new BikeResult(
                model.getId(),
                model.getName(),
                model.getType().name(),
                model.getStatus().name(),
                model.isPublic(),
                model.isExternalSync(),
                model.getBrand(),
                model.getModel(),
                model.getYear(),
                model.getSerialNumber(),
                model.getNotes(),
                model.getOdometerKm() == null ? 0 : model.getOdometerKm(),
                model.getUsageTimeMinutes() == null ? 0 : model.getUsageTimeMinutes(),
                model.getComponents().stream().map(BikeMapper::toComponentResult).collect(Collectors.toSet())
        );
    }

    public static BikeComponentResult toComponentResult(BikeComponent model) {
        return new BikeComponentResult(
                model.getId(),
                model.getComponentType(),
                model.getName(),
                model.getStatus().name(),
                model.getBrand(),
                model.getModel(),
                model.getNotes(),
                model.getOdometerKm() == null ? 0 : model.getOdometerKm(),
                model.getUsageTimeMinutes() == null ? 0 : model.getUsageTimeMinutes()
        );
    }
}
