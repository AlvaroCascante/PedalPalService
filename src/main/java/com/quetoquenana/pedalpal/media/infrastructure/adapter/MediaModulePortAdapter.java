package com.quetoquenana.pedalpal.media.infrastructure.adapter;

import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.application.port.MediaLookupPort;
import com.quetoquenana.pedalpal.media.application.query.MediaQueryService;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Infrastructure adapter that exposes media lookup via MediaLookupPort.
 */
@Component
@RequiredArgsConstructor
public class MediaModulePortAdapter implements MediaLookupPort {

    private final MediaQueryService queryService;

    /**
     * Returns media metadata for a given reference.
     */
    @Override
    public List<MediaResult> findByReferenceId(UUID referenceId, MediaReferenceType referenceType) {
        return queryService.getByReferenceIdAndReferenceType(referenceId, referenceType);
    }
}

