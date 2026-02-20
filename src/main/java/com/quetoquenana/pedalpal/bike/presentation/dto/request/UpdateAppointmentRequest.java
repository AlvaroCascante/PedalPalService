package com.quetoquenana.pedalpal.bike.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {
    private Instant appointmentDate;
    private Integer odometerKm;
    private String notes;
    private String status;
    private UUID storeLocationId;
}

