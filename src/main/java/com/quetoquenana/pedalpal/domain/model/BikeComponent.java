package com.quetoquenana.pedalpal.domain.model;

import com.quetoquenana.pedalpal.domain.enums.BikeComponentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
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
}
