package com.quetoquenana.pedalpal.domain.model;

import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
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
}
