package com.quetoquenana.pedalpal.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    @NotNull(message = "{appointment.create.bike.required}")
    private UUID bikeId;

    @NotNull(message = "{appointment.create.date.required}")
    private Instant appointmentDate;

    private Integer odometerKm;
    private String notes;
    private String status; // optional code like SCHEDULED
    private UUID storeLocationId;
}

