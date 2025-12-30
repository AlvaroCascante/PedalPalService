package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.UpdateBikeRequest;
import com.quetoquenana.pedalpal.model.Bike;
import com.quetoquenana.pedalpal.model.BikeComponent;

import java.util.UUID;

public interface BikeService {

    Bike createBike(CreateBikeRequest request);

    Bike updateBike(UUID id, UpdateBikeRequest request);

    void softDeleteBike(UUID id);

    BikeComponent addComponent(UUID bikeId, CreateComponentRequest request);
}
