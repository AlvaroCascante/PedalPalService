package com.quetoquenana.pedalpal.appointment.application.result;

import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;

public record ConfirmAppointmentResult(
        AppointmentResult appointmentResult,
        ServiceOrderResult serviceOrderResult
) {
}

