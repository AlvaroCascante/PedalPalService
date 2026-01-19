package com.quetoquenana.pedalpal.model.local;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateProductRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product extends Auditable {

    // JSON Views
    public static class ProductList extends ApiBaseResponseView.Always {}
    public static class ProductDetail extends Product.ProductList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JsonView(ProductList.class)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    @JsonView(ProductList.class)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    @JsonView(ProductDetail.class)
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    @JsonView(ProductList.class)
    private BigDecimal price;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @JsonView(ProductDetail.class)
    private SystemCode status;

    // Factory to create Product from DTO
    public static Product createFromRequest(CreateProductRequest request, SystemCode status) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(status)
                .build();
    }

    // Update fields from UpdateProductRequest
    public void updateFromRequest(UpdateProductRequest updateProductRequest, SystemCode status) {
        if (updateProductRequest.getName() != null) this.name = updateProductRequest.getName();
        if (updateProductRequest.getDescription() != null) this.description = updateProductRequest.getDescription();
        if (updateProductRequest.getPrice() != null) this.price = updateProductRequest.getPrice();
        if (status != null) this.status = status;
    }
}
