package com.quetoquenana.pedalpal.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "bike_components")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BikeComponent extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JsonView({Bike.BikeDetail.class, BikeComponentList.class})
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_type")
    @JsonView({Bike.BikeDetail.class, BikeComponentList.class})
    private SystemCode componentType;

    @Column(name = "name", nullable = false, length = 100)
    @JsonView({Bike.BikeDetail.class, BikeComponentList.class})
    private String name;

    @Column(name = "brand", length = 100)
    @JsonView({Bike.BikeDetail.class, BikeComponentList.class})
    private String brand;

    @Column(name = "model", length = 100)
    @JsonView({Bike.BikeDetail.class, BikeComponentList.class})
    private String model;

    @Column(name = "notes", columnDefinition = "text")
    @JsonView({BikeComponentDetail.class})
    private String notes;

    // create from DTO
    public static BikeComponent createFromRequest(
            Bike bike,
            CreateComponentRequest request,
            SystemCode componentType) {
        return BikeComponent.builder()
                .bike(bike)
                .componentType(componentType)
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .notes(request.getNotes())
                .build();
    }

    public void updateFromRequest(UpdateComponentRequest request, SystemCode componentType) {
        if (componentType != null) this.componentType = componentType;
        if (request.getName() != null) this.name = request.getName();
        if (request.getBrand() != null) this.brand = request.getBrand();
        if (request.getModel() != null) this.model = request.getModel();
        if (request.getNotes() != null) this.notes = request.getNotes();
    }

    // //JSON Views to control serialization responses
    public static class BikeComponentList extends ApiBaseResponseView.Always {}
    public static class BikeComponentDetail extends BikeComponent.BikeComponentList {}
}
