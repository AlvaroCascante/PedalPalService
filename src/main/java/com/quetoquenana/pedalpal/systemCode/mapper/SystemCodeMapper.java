package com.quetoquenana.pedalpal.systemCode.mapper;

import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemCodeMapper {

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
