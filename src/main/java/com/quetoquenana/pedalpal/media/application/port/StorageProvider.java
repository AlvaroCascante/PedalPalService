package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.media.domain.model.SignedUrl;

public interface StorageProvider {
    SignedUrl generateUploadUrl(String storageKey, String contentType, boolean isPublic);

    SignedUrl generateDownloadUrl(String storageKey, String contentType, boolean isPublic);
}
