package com.quetoquenana.pedalpal.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Appointment extends Auditable {

    private UUID id;

    private Bike bike;

    private StoreLocation storeLocation;

    private Instant appointmentDate;

    private Integer odometerKm;

    private BigDecimal totalCost;

    private String notes;

    private SystemCode status;

    private ProductPackage productPackage;

    private Set<Product> products;

    private Set<AppointmentTask> appointmentTasks;
}
