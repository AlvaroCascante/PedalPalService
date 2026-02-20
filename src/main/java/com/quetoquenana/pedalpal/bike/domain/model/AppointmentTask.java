package com.quetoquenana.pedalpal.bike.domain.model;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AppointmentTask extends Auditable {

    private UUID id;

    private Appointment appointment;

    private String name;

    private String description;
}
