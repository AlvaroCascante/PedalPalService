package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.BikeComponent;

import java.util.List;
import java.util.UUID;

public interface BikeRepositoryCustom {
    BikeComponent createComponent(UUID bikeId, BikeComponent component);
    BikeComponent updateComponent(BikeComponent component);
    void deleteComponent(UUID componentId);
    List<BikeComponent> findComponentsByBikeId(UUID bikeId);
}

