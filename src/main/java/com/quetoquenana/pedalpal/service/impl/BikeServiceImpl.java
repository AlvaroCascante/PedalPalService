package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.UpdateBikeRequest;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.Bike;
import com.quetoquenana.pedalpal.model.BikeComponent;
import com.quetoquenana.pedalpal.repository.BikeRepository;
import com.quetoquenana.pedalpal.service.BikeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;

    public BikeServiceImpl(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @Override
    @Transactional
    public Bike createBike(CreateBikeRequest request) {
        Bike bike = Bike.createFromRequest(request);
        return bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public Bike updateBike(UUID id, UpdateBikeRequest request) {
        Bike bike = bikeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found", id));
        bike.updateFromRequest(request);
        return bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public void softDeleteBike(UUID id) {
        Bike bike = bikeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found", id));
        bike.setStatus(com.quetoquenana.pedalpal.model.BikeStatus.INACTIVE);
        bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public BikeComponent addComponent(UUID bikeId, CreateComponentRequest request) {
        BikeComponent component = BikeComponent.createFromRequest(request);
        return bikeRepository.createComponent(bikeId, component);
    }
}

