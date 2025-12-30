package com.quetoquenana.pedalpal.model;

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
public class Store extends Auditable {

    // //JSON Views to control serialization responses
    public static class StoreList extends ApiBaseResponseView.Always {}
    public static class StoreDetail extends Store.StoreList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "website", length = 100)
    private String website;
}
