package com.quetoquenana.pedalpal.application.mapper;

import com.quetoquenana.pedalpal.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class BikeMapper {

    public static Bike toBike(CreateBikeCommand command) {
        return Bike.builder()
                .ownerId(command.ownerId())
                .name(command.name())
                .type(BikeType.valueOf(command.type()))
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
                .components(Collections.emptySet())
                .build();
    }

    public static BikeComponent toBikeComponent(AddBikeComponentCommand command) {
        return BikeComponent.builder()
                .name(command.name())
                .brand(command.brand())
                .model(command.model())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();
    }

    public static BikeResult toBikeResult(Bike bike) {
        Set<BikeComponent> components = bike.getComponents() == null ? Collections.emptySet() : bike.getComponents();
        return new BikeResult(
                bike.getId(),
                bike.getName(),
                bike.getType().name(),
                bike.getStatus().name(),
                bike.isPublic(),
                bike.isExternalSync(),
                bike.getBrand(),
                bike.getModel(),
                bike.getYear(),
                bike.getSerialNumber(),
                bike.getNotes(),
                bike.getOdometerKm() == null ? 0 : bike.getOdometerKm(),
                bike.getUsageTimeMinutes() == null ? 0 : bike.getUsageTimeMinutes(),
                components.stream().map(BikeMapper::toComponentResult).collect(Collectors.toSet())
        );
    }

    public static BikeComponentResult toComponentResult(BikeComponent component) {
        return new BikeComponentResult(
                component.getId(),
                component.getComponentType().getCode(),
                component.getName(),
                component.getBrand(),
                component.getModel(),
                component.getNotes(),
                component.getOdometerKm() == null ? 0 : component.getOdometerKm(),
                component.getUsageTimeMinutes() == null ? 0 : component.getUsageTimeMinutes()
        );
    }
}
