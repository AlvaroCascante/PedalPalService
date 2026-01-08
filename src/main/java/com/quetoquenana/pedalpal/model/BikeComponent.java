package com.quetoquenana.pedalpal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 100)
    private ComponentType componentType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "installed_at")
    private LocalDate installedAt;

    @Column(name = "installed_by", nullable = false, length = 100)
    private String installedBy;

    @Column(name = "installed_odometer_km")
    private Integer installedOdometerKm;

    @Column(name = "removed_at")
    private LocalDate removedAt;

    @Column(name = "removed_by", length = 100)
    private String removedBy;

    @Column(name = "removed_odometer_km")
    private Integer removedOdometerKm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installed_by_appointment_id")
    private Appointment installedByAppointment;

    // create from DTO
    public static BikeComponent createFromRequest(com.quetoquenana.pedalpal.dto.CreateComponentRequest r) {
        return BikeComponent.builder()
                .componentType(r.getComponentType())
                .name(r.getName())
                .brand(r.getBrand())
                .model(r.getModel())
                .notes(r.getNotes())
                .build();
    }

    public void updateFromRequest(com.quetoquenana.pedalpal.dto.UpdateComponentRequest r) {
        if (r.getComponentType() != null) this.componentType = r.getComponentType();
        if (r.getName() != null) this.name = r.getName();
        if (r.getBrand() != null) this.brand = r.getBrand();
        if (r.getModel() != null) this.model = r.getModel();
        if (r.getNotes() != null) this.notes = r.getNotes();
    }

    // //JSON Views to control serialization responses
    public static class BikeComponentList extends ApiBaseResponseView.Always {}
    public static class BikeComponentDetail extends BikeComponent.BikeComponentList {}
}
