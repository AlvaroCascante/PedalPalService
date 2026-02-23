package com.quetoquenana.pedalpal.product.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ProductPackage extends Auditable {

    private UUID id;

    private String name;
    private String description;

    private BigDecimal price;
    private GeneralStatus status;
    private final Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
    }
}
