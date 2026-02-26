package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.presentation.dto.response.SystemCodeResponse;

import java.util.UUID;

public final class TestSystemCodeData {

    private TestSystemCodeData() {
    }

    public static SystemCodeResult systemCodeResult(UUID id) {
        return SystemCodeResult.builder()
                .id(id)
                .category("COMPONENT_TYPE")
                .code("CHAIN")
                .label("Chain")
                .description("Chain")
                .codeKey("component.chain")
                .status(GeneralStatus.ACTIVE)
                .position(1)
                .build();
    }

    public static SystemCodeResponse systemCodeResponse(UUID id) {
        return new SystemCodeResponse(
                id,
                "CHAIN",
                "Chain",
                "ACTIVE",
                1
        );
    }
}
