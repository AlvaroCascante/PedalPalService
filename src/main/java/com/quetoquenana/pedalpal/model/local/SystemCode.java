package com.quetoquenana.pedalpal.model.local;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateSystemCodeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSystemCodeRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "system_codes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SystemCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label")
    @JsonView({Bike.BikeDetail.class,
            BikeComponent.BikeComponentList.class,
            Product.ProductDetail.class})
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "code_key")
    private String codeKey;

    @Column(name = "position")
    private Integer position;

    // Factory helper to create from request DTO
    public static SystemCode createFromRequest(CreateSystemCodeRequest request) {
        return SystemCode.builder()
                .category(request.getCategory())
                .code(request.getCode())
                .label(request.getLabel())
                .description(request.getDescription())
                .isActive(request.getIsActive() == null || request.getIsActive())
                .codeKey(request.getCodeKey())
                .position(request.getPosition())
                .build();
    }

    // Update this entity from request DTO
    public void updateFromRequest(UpdateSystemCodeRequest request) {
        if (request.getCategory() != null) this.category = request.getCategory();
        if (request.getCode() != null) this.code = request.getCode();
        if (request.getLabel() != null) this.label = request.getLabel();
        if (request.getDescription() != null) this.description = request.getDescription();
        if (request.getIsActive() != null) this.isActive = request.getIsActive();
        if (request.getCodeKey() != null) this.codeKey = request.getCodeKey();
        if (request.getPosition() != null) this.position = request.getPosition();
    }
}
