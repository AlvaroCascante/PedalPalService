package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.request.CreateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreLocationRequest;
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

    // Create a StoreLocation entity from CreateStoreLocationRequest and parent Store
    public static StoreLocation createFromRequest(CreateStoreLocationRequest request, Store store) {
        return StoreLocation.builder()
                .store(store)
                .name(request.getName())
                .website(request.getWebsite())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .phone(request.getPhone())
                .build();
    }

    // Update fields from UpdateStoreLocationRequest (partial)
    public void updateFromRequest(UpdateStoreLocationRequest request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getWebsite() != null) this.website = request.getWebsite();
        if (request.getAddress() != null) this.address = request.getAddress();
        if (request.getLatitude() != null) this.latitude = request.getLatitude();
        if (request.getLongitude() != null) this.longitude = request.getLongitude();
        if (request.getPhone() != null) this.phone = request.getPhone();
    }
}
