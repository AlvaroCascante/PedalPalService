package com.quetoquenana.pedalpal.product.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
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

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private GeneralStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "packages_products",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<ProductEntity> products;

    public void addProduct(ProductEntity product) {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.add(product);
    }
}

