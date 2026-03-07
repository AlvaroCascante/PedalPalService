package com.quetoquenana.pedalpal.serviceorder.application.port;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderResult;

import java.util.UUID;

public interface ServiceOrderPort {
    ServiceOrderResult cancelServiceOrder(UUID serviceOrder, String reason);

    ServiceOrderResult creteServiceOrder(Appointment appointment ,String locationPrefix);

}
