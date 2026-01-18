package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateComponentRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.BikeComponent;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.BikeComponentRepository;
import com.quetoquenana.pedalpal.service.BikeComponentService;
import com.quetoquenana.pedalpal.service.BikeService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.BikeComponents.COMPONENT_TYPE;

@Service
public class BikeComponentServiceImpl implements BikeComponentService {

    private final BikeComponentRepository componentRepository;
    private final BikeService bikeService;
    private final SystemCodeService systemCodeService;

    public BikeComponentServiceImpl(BikeComponentRepository componentRepository, BikeService bikeService,
                                    SystemCodeService systemCodeService) {
        this.componentRepository = componentRepository;
        this.bikeService = bikeService;
        this.systemCodeService = systemCodeService;
    }

    @Override
    @Transactional
    public BikeComponent replaceComponent(UUID componentId, CreateComponentRequest request) {
        BikeComponent existing = getIfOwnerOrAdmin(componentId);
        BikeComponent newComponent = bikeService.addComponent(existing.getBike().getId(), request);
        this.removeComponent(componentId);
        return newComponent;
    }

    @Override
    @Transactional
    public BikeComponent updateComponent(UUID componentId, UpdateComponentRequest request) {
        BikeComponent existing = getIfOwnerOrAdmin(componentId);

        SystemCode componentType = null;
        if (request.getComponentType() != null) {
            componentType = systemCodeService.findByCategoryAndCode(COMPONENT_TYPE, request.getComponentType())
                    .orElseThrow(() -> new RecordNotFoundException("component.type.not.found", request.getComponentType()));
        }

        existing.updateFromRequest(request, componentType);

        return componentRepository.save(existing);
    }

    @Override
    @Transactional
    public void removeComponent(UUID componentId) {
        BikeComponent existing = getIfOwnerOrAdmin(componentId);
        componentRepository.deleteById(existing.getId());
    }

    private BikeComponent getIfOwnerOrAdmin(UUID componentId) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        BikeComponent component;
        if (user.isAdmin()) {
            component = componentRepository.findById(componentId)
                    .orElseThrow(() -> new RecordNotFoundException("component.not.found", componentId));
        } else {
            component = componentRepository.findByIdAndBikeOwnerId(componentId, user.userId())
                    .orElseThrow(() -> new RecordNotFoundException("component.not.found", componentId));
        }
        component.setUpdatedAt(LocalDateTime.now());
        component.setUpdatedBy(user.username());
        return component;
    }
}

