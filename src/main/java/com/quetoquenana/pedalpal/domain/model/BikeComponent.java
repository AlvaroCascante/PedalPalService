package com.quetoquenana.pedalpal.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BikeComponent extends Auditable {

    private UUID id;

    private Bike bike;

    private SystemCode componentType;

    private String name;

    private String brand;

    private String model;

    private String notes;

    private Integer odometerKm;

    private Integer usageTimeMinutes;
}
