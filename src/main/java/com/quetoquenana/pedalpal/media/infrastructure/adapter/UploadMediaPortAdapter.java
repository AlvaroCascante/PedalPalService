package com.quetoquenana.pedalpal.media.infrastructure.adapter;

import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.useCase.UploadMediaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UploadMediaPortAdapter implements UploadMediaPort {

    private final UploadMediaUseCase uploadMediaUseCase;

    @Override
    public Set<UploadMediaResult> generateUploadUrls(UploadMediaCommand command) {
        return uploadMediaUseCase.execute(command);
    }
}
