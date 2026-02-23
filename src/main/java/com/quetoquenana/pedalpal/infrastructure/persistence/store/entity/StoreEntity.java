package com.quetoquenana.pedalpal.infrastructure.persistence.store.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quetoquenana.pedalpal.infrastructure.persistence.auditing.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stores")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<StoreLocationEntity> locations;

    public void addLocation(StoreLocationEntity location) {
        if (this.locations == null) {
            this.locations = new HashSet<>();
        }
        this.locations.add(location);
        location.setStore(this);
    }
}
