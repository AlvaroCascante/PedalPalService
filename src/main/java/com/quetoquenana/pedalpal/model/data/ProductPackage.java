package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ProductPackage extends Auditable {

    // JSON Views
    public static class PackageList extends ApiBaseResponseView.Always {}
    public static class PackageDetail extends ProductPackage.PackageList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCode status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "packages_products",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
}

