package com.quetoquenana.pedalpal.media.application.port;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;

import java.util.List;

public interface UploadMediaPort {

    List<MediaResult> generateUploadUrls(UploadMediaCommand uploadMediaCommand);
}
