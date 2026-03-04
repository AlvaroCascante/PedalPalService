package com.quetoquenana.pedalpal.store.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StoreLocation that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(website, that.website) && Objects.equals(address, that.address) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, website, address, latitude, longitude);
    }
}
