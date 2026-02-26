package com.quetoquenana.pedalpal.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.entity.SystemCodeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "maintenance_suggestion_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MaintenanceSuggestionItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id", nullable = false)
    private MaintenanceSuggestionEntity suggestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ProductPackageEntity productsPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority")
    private SystemCodeEntity priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "urgency")
    private SystemCodeEntity urgency;

    @Column(name = "reason", columnDefinition = "text", nullable = false)
    private String reason;
}
