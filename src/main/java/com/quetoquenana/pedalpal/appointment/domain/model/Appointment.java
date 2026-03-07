package com.quetoquenana.pedalpal.appointment.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal deposit;

    private List<RequestedService> requestedServices;

    /**
     * Applies a status transition and validates required data for specific targets.
     */
    public void changeStatusTo(AppointmentStatus toStatus, String closureReason, BigDecimal deposit) {
        AppointmentStatus fromStatus = this.status;

        if (toStatus == null || toStatus == AppointmentStatus.UNKNOWN) {
            throw new BadRequestException("appointment.status.invalid", toStatus);
        }

        if (!AppointmentTransitions.isAllowed(fromStatus, toStatus)) {
            throw new BusinessException("appointment.status.transition.invalid", fromStatus, toStatus);
        }

        if (toStatus == AppointmentStatus.CANCELED || toStatus == AppointmentStatus.REJECTED) {
            if (isBlank(closureReason)) {
                throw new BadRequestException("appointment.status.reason.required");
            }
            this.closureReason = closureReason.trim();
        } else {
            this.closureReason = null;
        }

        if (toStatus == AppointmentStatus.CONFIRMED) {
            if (deposit == null || deposit.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("appointment.status.deposit.required");
            }
            this.deposit = deposit;
        }

        this.status = toStatus;
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
