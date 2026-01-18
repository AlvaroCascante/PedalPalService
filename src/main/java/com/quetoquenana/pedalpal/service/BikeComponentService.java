package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateComponentRequest;
import com.quetoquenana.pedalpal.model.data.BikeComponent;

import java.util.UUID;

public interface BikeComponentService {

    BikeComponent replaceComponent(UUID componentId, CreateComponentRequest request);

    BikeComponent updateComponent(UUID componentId, UpdateComponentRequest request);

    void removeComponent(UUID componentId);
}

