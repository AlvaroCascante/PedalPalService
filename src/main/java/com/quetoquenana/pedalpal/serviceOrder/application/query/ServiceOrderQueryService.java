package com.quetoquenana.pedalpal.serviceOrder.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

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
}
