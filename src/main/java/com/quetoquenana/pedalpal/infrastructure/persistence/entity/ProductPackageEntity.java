package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "packages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductPackageEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCodeEntity status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "packages_products",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> products;
}
