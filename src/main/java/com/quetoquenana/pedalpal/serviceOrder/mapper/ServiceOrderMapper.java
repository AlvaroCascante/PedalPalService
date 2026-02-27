package com.quetoquenana.pedalpal.serviceOrder.mapper;

import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderDetailResult;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceOrderMapper {

    public ServiceOrderResult toResult(ServiceOrder model) {
        List<ServiceOrderDetailResult> requestedServices = model.getRequestedServices().stream()
                .map(this::toResult)
                .toList();

        return new ServiceOrderResult(
                model.getId(),
                model.getAppointmentId(),
                model.getBikeId(),
                model.getStartedAt(),
                model.getCompletedAt(),
                model.getStatus(),
                model.getTotalPrice(),
                requestedServices
        );
    }

    private ServiceOrderDetailResult toResult(ServiceOrderDetail model) {
        return new ServiceOrderDetailResult(
                model.getId(),
                model.getProductId(),
                model.getTechnicianId(),
                model.getProductNameSnapshot(),
                model.getPriceSnapshot(),
                model.getStatus(),
                model.getStartedAt(),
                model.getCompletedAt(),
                model.getNotes()
        );
    }
}
