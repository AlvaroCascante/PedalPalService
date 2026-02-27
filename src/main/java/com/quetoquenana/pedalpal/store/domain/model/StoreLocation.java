package com.quetoquenana.pedalpal.store.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class StoreLocation extends Auditable {
    private UUID id;
    private String name;
    private String website;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;
    private String timezone;
    private GeneralStatus status;
}
