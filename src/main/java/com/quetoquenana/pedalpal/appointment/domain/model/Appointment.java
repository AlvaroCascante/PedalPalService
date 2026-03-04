package com.quetoquenana.pedalpal.appointment.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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

    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }

    // Equality based on bikeId, storeLocationId, and scheduledAt, as they uniquely identify an appointment
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(bikeId, that.bikeId) && Objects.equals(storeLocationId, that.storeLocationId) && Objects.equals(scheduledAt, that.scheduledAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bikeId, storeLocationId, scheduledAt);
    }
}

