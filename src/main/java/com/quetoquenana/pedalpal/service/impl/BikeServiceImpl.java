package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.local.Bike;
import com.quetoquenana.pedalpal.model.local.BikeComponent;
import com.quetoquenana.pedalpal.model.local.SystemCode;
import com.quetoquenana.pedalpal.repository.BikeComponentRepository;
import com.quetoquenana.pedalpal.repository.BikeRepository;
import com.quetoquenana.pedalpal.service.BikeService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.BikeComponents.COMPONENT_TYPE;
import static com.quetoquenana.pedalpal.util.Constants.SystemCodes.*;

@Service
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;
    private final BikeComponentRepository componentRepository;
    private final SystemCodeService systemCodeService;

    public BikeServiceImpl(BikeRepository bikeRepository, BikeComponentRepository componentRepository, SystemCodeService systemCodeService) {
        this.bikeRepository = bikeRepository;
        this.componentRepository = componentRepository;
        this.systemCodeService = systemCodeService;
    }

    @Override
    public Bike findById(UUID bikeId) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Bike bike = bikeRepository.findById(bikeId).orElseThrow(
                () -> new RecordNotFoundException("bike.not.found", bikeId));

        if (!bike.getOwnerId().equals(user.userId()) && // User is not the owner
                !bike.isPublic() &&                     // Bike is not public
                !user.isAdmin()                        // User is not admin
        ) {
            throw new RecordNotFoundException("bike.not.found", bikeId);
        }
        return bike;
    }

    @Override
    @Transactional
    public Bike createBikeForCustomer(UUID idCustomer, CreateBikeRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );
        return createBike(user, idCustomer, request);
    }

    @Override
    @Transactional
    public Bike createBike(CreateBikeRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );
        return createBike(user, user.userId(), request);
    }

    private Bike createBike(SecurityUser user, UUID idCustomer, CreateBikeRequest request) {
        SystemCode typeCode = systemCodeService.findByCategoryAndCode(BIKE_TYPE, request.getType())
                .orElseThrow(() -> new RecordNotFoundException("bike.type.not.found", request.getType()));

        SystemCode statusCode = systemCodeService.findByCategoryAndCode(BIKE_STATUS, BIKE_STATUS_ACTIVE)
                .orElseThrow(() -> new RecordNotFoundException("bike.status.not.found", BIKE_STATUS_ACTIVE));

        Bike bike = Bike.createFromRequest(request, idCustomer, statusCode, typeCode);
        LocalDateTime now = LocalDateTime.now();
        bike.setCreatedAt(now);
        bike.setUpdatedAt(now);
        bike.setCreatedBy(user.username());
        bike.setUpdatedBy(user.username());
        return bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public Bike updateBike(UUID id, UpdateBikeRequest request) {
        SystemCode typeCode = null;
        SystemCode statusCode = null;
        if (request.getType() != null) {
            typeCode = systemCodeService.findByCategoryAndCode(BIKE_TYPE, request.getType())
                    .orElseThrow(() -> new RecordNotFoundException("bike.type.not.found", request.getType()));
        }
        if (request.getStatus() != null) {
            statusCode = systemCodeService.findByCategoryAndCode(BIKE_STATUS, request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("bike.status.not.found", request.getStatus()));
        }

        Bike bike = getBikeIfOwnerOrAdmin(id);
        bike.updateFromRequest(request, statusCode, typeCode);

        return bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public void softDeleteBike(UUID id) {
        SystemCode statusCode = systemCodeService.findByCategoryAndCode(BIKE_STATUS, BIKE_STATUS_DELETED)
                .orElseThrow(() -> new RecordNotFoundException("bike.status.not.found", BIKE_STATUS_DELETED));

        Bike bike = getBikeIfOwnerOrAdmin(id);
        bike.setStatus(statusCode);
        bikeRepository.save(bike);
    }

    @Override
    @Transactional
    public BikeComponent addComponent(UUID bikeId, CreateComponentRequest request) {
        Bike bike = getBikeIfOwnerOrAdmin(bikeId);

        SystemCode componentType = systemCodeService.findByCategoryAndCode(COMPONENT_TYPE, request.getComponentType())
                .orElseThrow(() -> new RecordNotFoundException("component.type.not.found", request.getComponentType()));

        BikeComponent component = BikeComponent.createFromRequest(bike, request, componentType);

        LocalDateTime now = LocalDateTime.now();
        component.setCreatedAt(now);
        component.setUpdatedAt(now);
        component.setCreatedBy(bike.getUpdatedBy());
        component.setUpdatedBy(bike.getUpdatedBy());

        return componentRepository.save(component);
    }

    @Override
    public List<Bike> findByStatuses(List<String> statusCodes) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        if (statusCodes == null || statusCodes.isEmpty()) {
            return bikeRepository.findByOwnerId(user.userId());
        }
        return bikeRepository.findByOwnerIdAndStatusCodes(user.userId(), statusCodes);
    }

    @Override
    public List<Bike> findActive() {
        return this.findByStatuses(List.of(BIKE_STATUS_ACTIVE));
    }

    @Override
    public List<Bike> findAll() {
        return this.findByStatuses(BIKE_ALL_STATUSES);
    }

    private Bike getBikeIfOwnerOrAdmin(UUID bikeId) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Bike bike;
        if (user.isAdmin()) {
            bike = bikeRepository.findById(bikeId)
                    .orElseThrow(() -> new RecordNotFoundException("bike.not.found", bikeId));
        } else {
            bike = bikeRepository.findByIdAndOwnerId(bikeId, user.userId())
                    .orElseThrow(() -> new RecordNotFoundException("bike.not.found", bikeId));
        }
        bike.setUpdatedAt(LocalDateTime.now());
        bike.setUpdatedBy(user.username());
        return bike;
    }
}
