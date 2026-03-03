package com.quetoquenana.pedalpal.common.application.port;

import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;

import java.util.Set;

public interface UploadMediaPort {

    Set<UploadMediaResult> generateUploadUrls(UploadMediaCommand uploadMediaCommand);
}
