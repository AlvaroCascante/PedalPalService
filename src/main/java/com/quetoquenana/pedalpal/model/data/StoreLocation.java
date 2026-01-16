package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "store_locations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreLocation extends Auditable {

    // //JSON Views to control serialization responses
    public static class StoreLocationList extends ApiBaseResponseView.Always {}
    public static class StoreLocationDetail extends StoreLocation.StoreLocationList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "website", length = 100)
    private String website;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "phone", length = 100)
    private String phone;

    // status_id references system_codes.id -> stored as a SystemCode relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private SystemCode status;
}
