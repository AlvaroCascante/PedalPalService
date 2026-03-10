package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.media.application.model.SignedUrl;

public interface MediaUrlProvider {
    SignedUrl generateUploadUrl(String storageKey, String contentType, boolean isPublic);

    SignedUrl generateDownloadUrl(String storageKey, String contentType, boolean isPublic);
}
