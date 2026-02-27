package com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity.SystemCodeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "bike_components")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BikeComponentEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    @JsonBackReference
    private BikeEntity bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_type")
    private SystemCodeEntity componentType;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private BikeComponentStatus status;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "odometer_km")
    private Integer odometerKm;

    @Column(name = "usage_time_minutes")
    private Integer usageTimeMinutes;
}
