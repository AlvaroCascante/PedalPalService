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
    private ExternalSyncProvider externalSyncProvider;
    private String externalGearId;

    private final Set<BikeComponent> components = new HashSet<>();

    public void addComponent(BikeComponent component) {
        components.add(component);
    }

    public Optional<BikeChangeItem> changeName(String newValue) {
        if (newValue == null || Objects.equals(this.name, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_NAME,
                this.name,
                newValue
        );
        this.name = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeType(BikeType newValue) {
        if (newValue == null || Objects.equals(this.type, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_TYPE,
                this.type,
                newValue
        );
        this.type = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeIsPublic(Boolean newValue) {
        if (newValue == null || this.isPublic == newValue) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_IS_PUBLIC,
                this.isPublic,
                newValue
        );
        this.isPublic = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeIsExternalSync(Boolean newValue) {
        if (newValue == null || this.isExternalSync == newValue) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_IS_EXTERNAL_SYNC,
                this.isExternalSync,
                newValue
        );
        this.isExternalSync = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeStatus(BikeStatus newValue) {
        if (newValue == null || Objects.equals(this.status, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_STATUS,
                this.status,
                newValue
        );
        this.status = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeBrand(String newValue) {
        if (newValue == null || Objects.equals(this.brand, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_BRAND,
                this.brand,
                newValue
        );
        this.brand = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeModel(String newValue) {
        if (newValue == null || Objects.equals(this.model, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_MODEL,
                this.model,
                newValue
        );
        this.model = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeYear(Integer newValue) {
        if (newValue == null || Objects.equals(this.year, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_YEAR,
                this.year,
                newValue
        );
        this.year = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeSerialNumber(String newValue) {
        if (newValue == null || Objects.equals(this.serialNumber, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_SERIAL_NUMBER,
                this.serialNumber,
                newValue
        );
        this.serialNumber = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeNotes(String newValue) {
        if (newValue == null || Objects.equals(this.notes, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_NOTES,
                this.notes,
                newValue
        );
        this.notes = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeOdometerKm(Integer newValue) {
        if (newValue == null || Objects.equals(this.odometerKm, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_ODOMETER,
                this.odometerKm,
                newValue
        );
        this.odometerKm = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeUsageTimeMinutes(Integer newValue) {
        if (newValue == null || Objects.equals(this.usageTimeMinutes, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_USAGE_MINUTES,
                this.usageTimeMinutes,
                newValue
        );
        this.usageTimeMinutes = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeExternalGearId(String newValue) {
        if (newValue == null || Objects.equals(this.externalGearId, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_EXTERNAL_GEAR_ID,
                this.usageTimeMinutes,
                newValue
        );
        this.externalGearId = newValue;
        return Optional.of(change);
    }

    public Optional<BikeChangeItem> changeExternalSyncProvider(ExternalSyncProvider newValue) {
        if (newValue == null || Objects.equals(this.externalSyncProvider, newValue)) {
            return Optional.empty();
        }

        BikeChangeItem change = BikeChangeItem.of(
                BikeField.BIKE_EXTERNAL_SYNC_PROVIDER,
                this.usageTimeMinutes,
                newValue
        );
        this.externalSyncProvider = newValue;
        return Optional.of(change);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bike bike)) return false;
        return Objects.equals(ownerId, bike.ownerId) && Objects.equals(name, bike.name) && type == bike.type && Objects.equals(brand, bike.brand) && Objects.equals(model, bike.model) && Objects.equals(year, bike.year) && Objects.equals(serialNumber, bike.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, name, type, brand, model, year, serialNumber);
    }

}
