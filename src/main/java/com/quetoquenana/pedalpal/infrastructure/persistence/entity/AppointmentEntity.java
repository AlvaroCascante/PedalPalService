package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.*;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.store.entity.StoreLocationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private BikeEntity bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_location_id")
    private StoreLocationEntity storeLocation;

    @Column(name = "appointment_date", nullable = false)
    private Instant appointmentDate;

    @Column(name = "odometer_km")
    private Integer odometerKm;

    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "notes")
    private String notes;

    // Status references system_codes.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private SystemCodeEntity status;

    // Service package selected for the appointment (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ProductPackageEntity productPackage;

    // Individual products selected for the appointment
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointments_products",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> product;

    // Add appointment tasks collection (mapped by appointment)
    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AppointmentTaskEntity> appointmentTask;
}
