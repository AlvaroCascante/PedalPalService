package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class Store extends Auditable {

    private UUID id;

    private String name;
}
