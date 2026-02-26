package com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppointmentEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Column(name = "store_location_id", nullable = false)
    private UUID storeLocationId;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<AppointmentServiceEntity> services;

    public void addRequestedService(AppointmentServiceEntity service) {
        if (this.services == null) {
            this.services = new HashSet<>();
        }
        services.add(service);
        service.setAppointment(this);
    }
}

