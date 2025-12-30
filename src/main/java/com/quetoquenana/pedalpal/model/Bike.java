package com.quetoquenana.pedalpal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
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
@Table(name = "bikes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Bike extends Auditable {

    // //JSON Views to control serialization responses
    public static class BikeList extends ApiBaseResponseView.Always {}
    public static class BikeDetail extends Bike.BikeList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "owner_username", nullable = false, length = 100)
    private String ownerUsername;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 100)
    private BikeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private BikeStatus status;

    // Create a Bike entity from a CreateBikeRequest DTO
    public static Bike createFromRequest(com.quetoquenana.pedalpal.dto.CreateBikeRequest request) {
        return Bike.builder()
                .ownerUsername(request.getOwnerUsername())
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .type(request.getType())
                .serialNumber(request.getSerialNumber())
                .notes(request.getNotes())
                .status(BikeStatus.ACTIVE)
                .build();
    }

    // Update an existing Bike entity from UpdateBikeRequest DTO
    public void updateFromRequest(com.quetoquenana.pedalpal.dto.UpdateBikeRequest request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getBrand() != null) this.brand = request.getBrand();
        if (request.getModel() != null) this.model = request.getModel();
        if (request.getYear() != null) this.year = request.getYear();
        if (request.getType() != null) this.type = request.getType();
        if (request.getSerialNumber() != null) this.serialNumber = request.getSerialNumber();
        if (request.getNotes() != null) this.notes = request.getNotes();
        if (request.getStatus() != null) this.status = request.getStatus();
    }
}
