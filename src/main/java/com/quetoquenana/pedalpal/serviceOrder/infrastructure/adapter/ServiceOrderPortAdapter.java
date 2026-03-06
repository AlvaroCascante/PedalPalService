package com.quetoquenana.pedalpal.serviceOrder.infrastructure.adapter;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.application.port.ServiceOrderPort;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServiceOrderPortAdapter implements ServiceOrderPort {

    private final ServiceOrderMapper mapper;
    private final ServiceOrderRepository repository;

    @Value("${app.service.order.number.prefix}")
    private String serviceOrderNumberPrefix;

    @Override
    public ServiceOrderResult creteServiceOrder(Appointment appointment, String locationPrefix) {
        ServiceOrder model = ServiceOrder.createFromAppointment(appointment);
        model.setOrderNumber(serviceOrderNumberPrefix + "-" + Year.now().getValue() + "-" + String.format("%06d", repository.nextOrderSequence()));
        model = repository.save(model);

        return mapper.toResult(model);
    }

    @Override
    public ServiceOrderResult cancelServiceOrder(UUID serviceOrder, String reason) {
        ServiceOrder model = repository.findByAppointmentId(serviceOrder)
                .orElseThrow(() -> new RecordNotFoundException("serviceOrder.not.found"));

        model.cancel(ServiceOrderStatus.CANCELED, reason);
        repository.save(model);

        return mapper.toResult(model);
    }
}
