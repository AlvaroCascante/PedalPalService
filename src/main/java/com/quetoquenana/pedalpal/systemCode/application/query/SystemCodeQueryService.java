package com.quetoquenana.pedalpal.systemCode.application.query;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.systemCode.application.mapper.SystemCodeMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

/**
 * Read-only query service for SystemCode data.
 */
@RequiredArgsConstructor
public class SystemCodeQueryService {

    private final SystemCodeMapper mapper;
    private final SystemCodeRepository repository;

    /**
     * Retrieves a system code by id.
     */
    public SystemCodeResult getById(UUID id) {
        SystemCode model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    /**
     * Retrieves system codes by category and status.
     */
    public List<SystemCodeResult> getByCategoryAndStatus(String category, GeneralStatus status) {
        List<SystemCode> models = repository.findByCategoryAndStatus(category, status);

        return models.stream()
                .map(mapper::toResult)
                .toList();
    }

    /**
     * Retrieves active component system codes.
     */
    public List<SystemCodeResult> getActiveComponents() {
        return getByCategoryAndStatus(COMPONENT_TYPE, GeneralStatus.ACTIVE);
    }
}
