package com.quetoquenana.pedalpal.model.local;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
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
@Table(name = "stores")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Store extends Auditable {

    // //JSON Views to control serialization responses
    public static class StoreList extends ApiBaseResponseView.Always {}
    public static class StoreDetail extends Store.StoreList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JsonView(StoreList.class)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    @JsonView(StoreList.class)
    private String name;

    // Create a Store entity from a CreateStoreRequest DTO
    public static Store createFromRequest(CreateStoreRequest request) {
        return Store.builder()
                .name(request.getName())
                .build();
    }

    // Update this Store from UpdateStoreRequest (partial updates)
    public void updateFromRequest(UpdateStoreRequest request) {
        if (request.getName() != null) this.name = request.getName();
    }
}
