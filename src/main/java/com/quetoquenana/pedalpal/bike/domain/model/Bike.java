package com.quetoquenana.pedalpal.bike.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.util.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bike extends Auditable {
    private UUID id;
    private UUID ownerId;
    private String name;
    private BikeType type;
    private boolean isPublic;
    private boolean isExternalSync;
    private BikeStatus status;
    private String brand;
    private String model;
    private Integer year;
    private String serialNumber;
    private String notes;
    private Integer odometerKm;
    private Integer usageTimeMinutes;

    private final Set<BikeComponent> components = new HashSet<>();

    public void addComponent(BikeComponent component) {
        components.add(component);
    }

    public Optional<BikeChangeItem> changeName(String newName) {
        if (newName == null || Objects.equals(this.name, newName)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_NAME,
                this.name,
                newName
        );
        this.name = newName;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeType(BikeType newType) {
        if (newType == null || Objects.equals(this.type, newType)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_TYPE,
                this.type,
                newType
        );
        this.type = newType;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeIsPublic(Boolean newIsPublic) {
        if (newIsPublic == null || this.isPublic == newIsPublic) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_IS_PUBLIC,
                this.isPublic,
                newIsPublic
        );
        this.isPublic = newIsPublic;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeIsExternalSync(Boolean newIsExternalSync) {
        if (newIsExternalSync == null || this.isExternalSync == newIsExternalSync) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_IS_EXTERNAL_SYNC,
                this.isExternalSync,
                newIsExternalSync
        );
        this.isExternalSync = newIsExternalSync;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeStatus(BikeStatus newStatus) {
        if (newStatus == null || Objects.equals(this.status, newStatus)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_STATUS,
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
                BikeField.BIKE_BRAND,
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
                BikeField.BIKE_MODEL,
                this.model,
                newModel
        );
        this.model = newModel;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeYear(Integer newYear) {
        if (newYear == null || Objects.equals(this.year, newYear)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_YEAR,
                this.year,
                newYear
        );
        this.year = newYear;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeSerialNumber(String newSerialNumber) {
        if (newSerialNumber == null || Objects.equals(this.serialNumber, newSerialNumber)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_SERIAL_NUMBER,
                this.serialNumber,
                newSerialNumber
        );
        this.serialNumber = newSerialNumber;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeNotes(String newNotes) {
        if (newNotes == null || Objects.equals(this.notes, newNotes)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_NOTES,
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
                BikeField.BIKE_ODOMETER,
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
                BikeField.BIKE_USAGE_MINUTES,
                this.usageTimeMinutes,
                newUsageTimeMinutes
        );
        this.usageTimeMinutes = newUsageTimeMinutes;
        return Optional.of(change);
    }
}
