package com.quetoquenana.pedalpal.media.infrastructure.adapter;

import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.useCase.MediaUploadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Infrastructure adapter that exposes media upload capabilities via UploadMediaPort.
 */
@Component
@RequiredArgsConstructor
public class UploadMediaPortAdapter implements UploadMediaPort {

    private final MediaUploadUseCase useCase;

    /**
     * Generates signed upload URLs for media specs.
     */
    @Override
    public Set<UploadMediaResult> generateUploadUrls(UploadMediaCommand command) {
        return useCase.execute(command);
    }
}
