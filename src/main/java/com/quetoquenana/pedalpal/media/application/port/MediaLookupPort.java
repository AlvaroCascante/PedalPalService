package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;

import java.util.List;
import java.util.UUID;

public interface MediaLookupPort {
    List<MediaResult> findByReferenceId(UUID referenceId, String referenceType);
}
