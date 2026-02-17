package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "store_locations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreLocationEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "website", length = 100)
    private String website;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "phone", length = 100)
    private String phone;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCodeEntity status;
}
