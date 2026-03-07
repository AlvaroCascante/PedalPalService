package com.quetoquenana.pedalpal.serviceorder.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ServiceOrderQueryService {

    private final ServiceOrderMapper mapper;
    private final ServiceOrderRepository repository;

    public ServiceOrderResult getById(UUID id) {
        ServiceOrder model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    public List<ServiceOrderResult> getByBikeId(UUID bikeId) {
        List<ServiceOrder> models = repository.findByBikeId(bikeId);
        return models.stream().map(mapper::toResult).toList();
    }

}
