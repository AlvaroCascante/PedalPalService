package com.quetoquenana.pedalpal.serviceOrder.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderDetail {
    private UUID id;
    private UUID productId;
    private UUID technicianId;
    private String productNameSnapshot;
    private BigDecimal priceSnapshot;
    private ServiceOrderDetailStatus status;
    private Instant startedAt;
    private Instant completedAt;
    private String notes;
}

