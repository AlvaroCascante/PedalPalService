package com.quetoquenana.pedalpal.domain.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppointmentTask extends Auditable {

    private UUID id;

    private Appointment appointment;

    private String name;

    private String description;
}
