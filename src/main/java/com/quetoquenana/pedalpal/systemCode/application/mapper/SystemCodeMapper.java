package com.quetoquenana.pedalpal.systemCode.application.mapper;

import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Maps SystemCode domain models to application results.
 */
@Component
@RequiredArgsConstructor
public class SystemCodeMapper {

    /**
     * Builds a result DTO from a domain model.
     */
    public SystemCodeResult toResult(SystemCode model) {
        return new SystemCodeResult(
                model.getId(),
                model.getCategory(),
                model.getCode(),
                model.getLabel(),
                model.getDescription(),
                model.getCodeKey(),
                model.getStatus(),
                model.getPosition()
        );
    }
}
