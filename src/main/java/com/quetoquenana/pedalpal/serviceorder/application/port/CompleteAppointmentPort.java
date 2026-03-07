package com.quetoquenana.pedalpal.serviceorder.application.port;

import java.util.UUID;

/**
 * Port used by service-order transitions to synchronize appointment status.
 */
public interface CompleteAppointmentPort {

    /**
     * Marks the related appointment as completed.
     */
    void completeAppointment(UUID appointmentId);
}
