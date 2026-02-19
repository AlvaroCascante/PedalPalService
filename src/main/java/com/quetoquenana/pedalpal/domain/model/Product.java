package com.quetoquenana.pedalpal.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Product extends Auditable {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private SystemCode status;
}
