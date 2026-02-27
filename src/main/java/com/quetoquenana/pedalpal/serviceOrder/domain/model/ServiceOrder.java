package com.quetoquenana.pedalpal.serviceOrder.domain.model;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.common.domain.model.Auditable;
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
public class ServiceOrder extends Auditable {

    private UUID id;
    private UUID appointmentId;
    private UUID bikeId;
    private Instant startedAt;
    private Instant completedAt;
    private BigDecimal totalPrice;
    private ServiceOrderStatus status;

    private List<ServiceOrderDetail> requestedServices;

    public static ServiceOrder createFromAppointment(Appointment appointment) {
        List<ServiceOrderDetail> details = appointment.getRequestedServices().stream()
                .map(service -> ServiceOrderDetail.builder()
                        .productId(service.getServiceId())
                        .productNameSnapshot(service.getName())
                        .priceSnapshot(service.getPrice())
                        .build())
                .toList();

        BigDecimal total = details.stream()
                .map(ServiceOrderDetail::getPriceSnapshot)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ServiceOrder.builder()
                .appointmentId(appointment.getId())
                .bikeId(appointment.getBikeId())
                .status(ServiceOrderStatus.CREATED)
                .totalPrice(total)
                .requestedServices(details)
                .build();
    }

    public void addRequestedService(ServiceOrderDetail service) {
        if (this.requestedServices == null) {
            this.requestedServices = new java.util.ArrayList<>();
        }
        requestedServices.add(service);
    }
}
