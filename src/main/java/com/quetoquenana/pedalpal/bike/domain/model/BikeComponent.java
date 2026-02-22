package com.quetoquenana.pedalpal.bike.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import lombok.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BikeComponent extends Auditable {
    private UUID id;
    private SystemCode componentType;
    private String name;
    private BikeComponentStatus status;
    private String brand;
    private String model;
    private String notes;
    private Integer odometerKm;
    private Integer usageTimeMinutes;

    public Optional<BikeChangeItem> changeComponentType(SystemCode componentType) {
        if (componentType == null || Objects.equals(this.componentType, componentType)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_TYPE,
                this.componentType,
                componentType
        );
        this.componentType = componentType;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeName(String newName) {
        if (newName == null || Objects.equals(this.name, newName)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_NAME,
                this.name,
                newName
        );
        this.name = newName;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeStatus(BikeComponentStatus newStatus) {
        if (newStatus == null || Objects.equals(this.status, newStatus)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_STATUS,
                this.status,
                newStatus
        );
        this.status = newStatus;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeBrand(String newBrand) {
        if (newBrand == null || Objects.equals(this.brand, newBrand)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_BRAND,
                this.brand,
                newBrand
        );
        this.brand = newBrand;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeModel(String newModel) {
        if (newModel == null || Objects.equals(this.model, newModel)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_MODEL,
                this.model,
                newModel
        );
        this.model = newModel;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeNotes(String newNotes) {
        if (newNotes == null || Objects.equals(this.notes, newNotes)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_NOTES,
                this.notes,
                newNotes
        );
        this.notes = newNotes;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeOdometerKm(Integer newOdometerKm) {
        if (newOdometerKm == null || Objects.equals(this.odometerKm, newOdometerKm)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_ODOMETER,
                this.odometerKm,
                newOdometerKm
        );
        this.odometerKm = newOdometerKm;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeUsageTimeMinutes(Integer newUsageTimeMinutes) {
        if (newUsageTimeMinutes == null || Objects.equals(this.usageTimeMinutes, newUsageTimeMinutes)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_COMPONENT_USAGE_MINUTES,
                this.usageTimeMinutes,
                newUsageTimeMinutes
        );
        this.usageTimeMinutes = newUsageTimeMinutes;
        return Optional.of(change);
    }
}
