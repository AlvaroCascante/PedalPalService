package com.quetoquenana.pedalpal.infrastructure.persistence.maintenanceSuggestion.entity;

import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
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
    private ProductPackageEntity productPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "priority")
    private String priority;

    @Column(name = "urgency")
    private String urgency;

    @Column(name = "reason", columnDefinition = "text", nullable = false)
    private String reason;
}
