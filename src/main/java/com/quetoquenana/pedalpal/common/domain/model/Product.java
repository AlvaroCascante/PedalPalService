package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
