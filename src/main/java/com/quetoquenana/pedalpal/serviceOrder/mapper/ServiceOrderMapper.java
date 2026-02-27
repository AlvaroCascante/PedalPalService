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

        return ServiceOrderResult.builder()
                .id(model.getId())
                .appointmentId(model.getAppointmentId())
                .bikeId(model.getBikeId())
                .status(model.getStatus())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .totalPrice(model.getTotalPrice())
                .requestedServices(requestedServices)
                .build();
    }

    private ServiceOrderDetailResult toResult(ServiceOrderDetail model) {
        return ServiceOrderDetailResult.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .technicianId(model.getTechnicianId())
                .productName(model.getProductNameSnapshot())
                .price(model.getPriceSnapshot())
                .status(model.getStatus())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .notes(model.getNotes())
                .build();
    }
}

