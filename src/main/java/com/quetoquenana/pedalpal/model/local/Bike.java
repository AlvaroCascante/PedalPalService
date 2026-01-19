package com.quetoquenana.pedalpal.model.local;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bikes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bike extends Auditable {

    // //JSON Views to control serialization responses
    public static class BikeList extends ApiBaseResponseView.Always {}
    public static class BikeDetail extends Bike.BikeList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JsonView(BikeList.class)
    private UUID id;

    @Column(name = "owner_id", nullable = false, length = 100)
    @JsonView(BikeDetail.class)
    private UUID ownerId;

    @Column(name = "name", nullable = false, length = 100)
    @JsonView(BikeList.class)
    private String name;

    @Column(name = "brand", length = 100)
    @JsonView(BikeList.class)
    private String brand;

    @Column(name = "model", length = 100)
    @JsonView(BikeList.class)
    private String model;

    @Column(name = "year")
    @JsonView(BikeDetail.class)
    private Integer year;

    @Column(name = "serial_number", length = 100)
    @JsonView(BikeList.class)
    private String serialNumber;

    @Column(name = "notes", columnDefinition = "text")
    @JsonView(BikeList.class)
    private String notes;

    @Column(name = "is_public", nullable = false)
    @JsonView(BikeList.class)
    private boolean isPublic;

    // type_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @JsonView(BikeDetail.class)
    private SystemCode type;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @JsonView(BikeDetail.class)
    private SystemCode status;

    // components for this bike
    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonView(BikeDetail.class)
    private List<BikeComponent> components;

    // Create a Bike entity from a CreateBikeRequest DTO
    public static Bike createFromRequest(CreateBikeRequest request,
                                         UUID idCustomer,
                                         SystemCode statusCode,
                                         SystemCode typeCode) {
        return Bike.builder()
                .ownerId(idCustomer)
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .serialNumber(request.getSerialNumber())
                .notes(request.getNotes())
                .status(statusCode)
                .type(typeCode)
                .isPublic(request.isPublic())
                .build();
    }

    // Update an existing Bike entity from UpdateBikeRequest DTO
    public void updateFromRequest(UpdateBikeRequest request,
                                  SystemCode statusCode,
                                  SystemCode typeCode) {
        if (request.getType() != null) this.type = typeCode;
        if (request.getStatus() != null) this.status = statusCode;
        if (request.getName() != null) this.name = request.getName();
        if (request.getBrand() != null) this.brand = request.getBrand();
        if (request.getModel() != null) this.model = request.getModel();
        if (request.getYear() != null) this.year = request.getYear();
        if (request.getSerialNumber() != null) this.serialNumber = request.getSerialNumber();
        if (request.getNotes() != null) this.notes = request.getNotes();
        this.isPublic = request.isPublic();
    }
}
