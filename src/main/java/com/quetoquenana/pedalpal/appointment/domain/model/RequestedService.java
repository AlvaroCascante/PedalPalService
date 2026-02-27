package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestedService {
    private UUID id;
    private UUID serviceId;
    private String name;
    private BigDecimal price;
}

