package com.quetoquenana.pedalpal.util;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.CreateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.request.UpdateAnnouncementStatusRequest;
import com.quetoquenana.pedalpal.announcement.presentation.dto.response.AnnouncementResponse;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;

import java.util.UUID;

public final class TestAnnouncementData {

    private TestAnnouncementData() {
    }

    public static CreateAnnouncementRequest createRequest() {
        return new CreateAnnouncementRequest(
                "Title",
                "Subtitle",
                "Description",
                1,
                "https://example.com"
        );
    }

    public static UpdateAnnouncementRequest updateRequest_onlyTitle() {
        return new UpdateAnnouncementRequest(
                "New title",
                null,
                null,
                null,
                null
        );
    }

    public static UpdateAnnouncementStatusRequest updateStatusRequest(String status) {
        return new UpdateAnnouncementStatusRequest(status);
    }

    public static CreateAnnouncementCommand createCommand(UUID authUserId) {
        return new CreateAnnouncementCommand(
                authUserId,
                "Title",
                "Subtitle",
                "Description",
                1,
                "https://example.com"
        );
    }

    public static UpdateAnnouncementCommand updateCommand_onlyTitle(UUID id, UUID authUserId) {
        return new UpdateAnnouncementCommand(
                id,
                authUserId,
                "New title",
                null,
                null,
                null,
                null
        );
    }

    public static UpdateAnnouncementStatusCommand statusCommand(UUID id, String status) {
        return new UpdateAnnouncementStatusCommand(id, status);
    }

    public static Announcement existingAnnouncement(UUID id) {
        return Announcement.builder()
                .id(id)
                .title("Old title")
                .subTitle("Old subtitle")
                .description("Old description")
                .position(1)
                .url("https://old.example.com")
                .status(GeneralStatus.ACTIVE)
                .build();
    }

    public static AnnouncementResult result(UUID id) {
        return AnnouncementResult.builder()
                .id(id)
                .title("Title")
                .subTitle("Subtitle")
                .description("Description")
                .position(1)
                .url("https://example.com")
                .status(GeneralStatus.ACTIVE)
                .build();
    }

    public static AnnouncementResponse response(UUID id) {
        return new AnnouncementResponse(
                id,
                "Title",
                "Subtitle",
                "Description",
                1,
                "https://example.com",
                "Active"
        );
    }
}
