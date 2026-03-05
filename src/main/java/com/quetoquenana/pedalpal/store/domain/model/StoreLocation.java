package com.quetoquenana.pedalpal.store.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model for store locations with basic invariants.
 */
@Getter
@Setter
public class StoreLocation extends Auditable {
    private UUID id;
    private String name;
    private String storePrefix;
    private String website;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String timezone;
    private GeneralStatus status;

    /**
     * Creates a StoreLocation with validated core fields.
     */
    @Builder
    public StoreLocation(UUID id,
                         String name,
                         String storePrefix,
                         String website,
                         String address,
                         BigDecimal latitude,
                         BigDecimal longitude,
                         String phone,
                         String timezone,
                         GeneralStatus status) {
        validateNotBlank("name", name);
        validateStatus(status);
        this.id = id;
        this.name = name;
        this.storePrefix = storePrefix;
        this.website = website;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.timezone = timezone;
        this.status = status;
    }

    /**
     * Updates the name value with validation.
     */
    public void setName(String name) {
        validateNotBlank("name", name);
        this.name = name;
    }

    /**
     * Updates the status value with validation.
     */
    public void setStatus(GeneralStatus status) {
        validateStatus(status);
        this.status = status;
    }

    private static void validateNotBlank(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

    private static void validateStatus(GeneralStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StoreLocation that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(website, that.website)
                && Objects.equals(address, that.address)
                && Objects.equals(latitude, that.latitude)
                && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, website, address, latitude, longitude);
    }
}
