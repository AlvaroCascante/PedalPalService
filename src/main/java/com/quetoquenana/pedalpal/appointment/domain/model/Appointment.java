package com.quetoquenana.pedalpal.appointment.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
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
    private UUID customerId;
    private UUID storeLocationId;
    private Instant scheduledAt;
    private AppointmentStatus status;
    private String notes;
    private String closureReason;

    private List<RequestedService> requestedServices;

    /**
     * Applies a status transition and validates mandatory closure reason for canceled/rejected statuses.
     */
    public void changeStatusTo(AppointmentStatus toStatus, AppointmentStatusChangeContext context) {
        AppointmentStatus fromStatus = this.status;

        if (toStatus == null || toStatus == AppointmentStatus.UNKNOWN) {
            throw new BadRequestException("appointment.status.invalid", toStatus);
        }

        if (!AppointmentTransitions.isAllowed(fromStatus, toStatus)) {
            throw new BusinessException("appointment.status.transition.invalid", fromStatus, toStatus);
        }

        if (toStatus == AppointmentStatus.CANCELED || toStatus == AppointmentStatus.REJECTED) {
            if (context == null || isBlank(context.closureReason())) {
                throw new BadRequestException("appointment.status.reason.required");
            }
            this.closureReason = context.closureReason().trim();
        } else {
            this.closureReason = null;
        }

        this.status = toStatus;
    }

    public record AppointmentStatusChangeContext(
            UUID authenticatedUserId,
            UUID technicianId,
            String closureReason,
            String note
    ) {
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
