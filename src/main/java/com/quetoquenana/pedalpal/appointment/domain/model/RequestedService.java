package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
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

    // Equality based on serviceId, as it uniquely identifies a requested service
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RequestedService that)) return false;
        return Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceId);
    }
}

