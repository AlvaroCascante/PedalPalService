package com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quetoquenana.pedalpal.bike.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.enums.BikeType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bikes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BikeEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private BikeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private BikeStatus status;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "is_external_sync")
    private boolean isExternalSync;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "serial_number", unique = true, length = 100)
    private String serialNumber;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "odometer_km")
    private Integer odometerKm;

    @Column(name = "usage_time_minutes")
    private Integer usageTimeMinutes;

    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<BikeComponentEntity> components;

    public void addComponent(BikeComponentEntity component) {
        if (this.components == null) {
            this.components = new HashSet<>();
        }
        this.components.add(component);
        component.setBike(this);
    }
}
