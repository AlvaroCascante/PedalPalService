package com.quetoquenana.pedalpal.model.data;

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
public class BikeComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_type")
    private SystemCode componentType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCode status;

    // create from DTO
    public static BikeComponent createFromRequest(CreateComponentRequest request, SystemCode componentType) {
        return BikeComponent.builder()
                .componentType(componentType)
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .notes(request.getNotes())
                .build();
    }

    public void updateFromRequest(UpdateComponentRequest request, SystemCode componentType, SystemCode status) {
        if (componentType != null) this.componentType = componentType;
        if (status != null) this.status = status;
        if (request.getName() != null) this.name = request.getName();
        if (request.getBrand() != null) this.brand = request.getBrand();
        if (request.getModel() != null) this.model = request.getModel();
        if (request.getNotes() != null) this.notes = request.getNotes();
    }

    // //JSON Views to control serialization responses
    public static class BikeComponentList extends ApiBaseResponseView.Always {}
    public static class BikeComponentDetail extends BikeComponent.BikeComponentList {}
}
