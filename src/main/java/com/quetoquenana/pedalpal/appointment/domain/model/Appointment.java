package com.quetoquenana.pedalpal.appointment.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends Auditable {

    private UUID id;
    private UUID bikeId;
    private UUID storeLocationId;
    private Instant scheduledAt;
    private AppointmentStatus status;
    private String notes;

    private List<RequestedService> requestedServices;

    public void addRequestedService(RequestedService service) {
        if (this.requestedServices == null) {
            this.requestedServices = new java.util.ArrayList<>();
        }
        requestedServices.add(service);
    }

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }
}

