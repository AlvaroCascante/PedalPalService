package com.quetoquenana.pedalpal.appointment.domain.model;

import com.quetoquenana.pedalpal.product.domain.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentService {
    private UUID id;
    private Product product;
    private String productNameSnapshot;
    private BigDecimal priceSnapshot;
}

