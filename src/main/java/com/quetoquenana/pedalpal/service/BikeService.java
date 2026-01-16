package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.model.data.Bike;

import java.util.List;
import java.util.UUID;

public interface BikeService {

    Bike findById(UUID bikeId);

    Bike createBike(CreateBikeRequest request);

    Bike updateBike(UUID id, UpdateBikeRequest request);

    void softDeleteBike(UUID id);

    List<Bike> findByStatuses(List<String> statusCodes);

    List<Bike> findActive();

    List<Bike> findAll();
}
