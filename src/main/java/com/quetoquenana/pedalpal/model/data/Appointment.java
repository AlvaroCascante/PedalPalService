package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Appointment extends Auditable {

    // //JSON Views to control serialization responses
    public static class AppointmentList extends ApiBaseResponseView.Always {}
    public static class AppointmentDetail extends Appointment.AppointmentList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_location_id")
    private StoreLocation storeLocation;

    @Column(name = "appointment_date", nullable = false)
    private Instant appointmentDate;

    @Column(name = "odometer_km")
    private Integer odometerKm;

    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "notes", length = 255)
    private String notes;

    // Status references system_codes.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private SystemCode status;

    // Service package selected for the appointment (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ProductPackage productPackage;

    // Individual products selected for the appointment
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointments_products",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
}
