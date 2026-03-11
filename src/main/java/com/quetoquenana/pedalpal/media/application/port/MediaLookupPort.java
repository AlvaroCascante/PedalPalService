package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;

import java.util.List;
import java.util.UUID;

public interface MediaLookupPort {
    List<MediaResult> findByReferenceId(UUID referenceId, MediaReferenceType referenceType);
}
