package com.quetoquenana.pedalpal.systemCode.application.query;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.systemCode.mapper.SystemCodeMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@RequiredArgsConstructor
public class SystemCodeQueryService {

    private final SystemCodeMapper mapper;
    private final SystemCodeRepository repository;

    public SystemCodeResult getById(UUID id) {
        SystemCode model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    public List<SystemCodeResult> getActiveComponents() {
        List<SystemCode> models = repository.findByCategoryAndStatus(
                COMPONENT_TYPE,
                GeneralStatus.ACTIVE
        );

        return models.stream()
                .map(mapper::toResult)
                .toList();
    }
}
