package com.quetoquenana.pedalpal.announcement.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.CreateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.util.TestAnnouncementData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAnnouncementUseCaseTest {

    @Mock
    AnnouncementRepository repository;

    @Mock
    AnnouncementMapper mapper;

    @Mock
    UploadMediaPort uploadMediaPort;

    @InjectMocks
    CreateAnnouncementUseCase useCase;

    @Captor
    ArgumentCaptor<Announcement> announcementCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldCreateAnnouncement_whenCommandIsValid() {
            UUID authUserId = UUID.randomUUID();
            UUID savedId = UUID.randomUUID();

            CreateAnnouncementCommand command = TestAnnouncementData.createCommand(authUserId);

            Announcement model = Announcement.builder()
                    .title(command.title())
                    .subTitle(command.subTitle())
                    .description(command.description())
                    .position(command.position())
                    .url(command.url())
                    .status(MediaStatus.ACTIVE)
                    .build();

            Announcement saved = TestAnnouncementData.existingAnnouncement(savedId);

            UploadMediaCommand mediaCommand = new UploadMediaCommand(
                    authUserId,
                    command.isAdmin(),
                    command.isPublic(),
                    savedId,
                    com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType.ANNOUNCEMENT,
                    java.util.Set.of()
            );
            Set<UploadMediaResult> mediaResult = Set.of(
                    new UploadMediaResult(UUID.randomUUID(), "announcements/key", "https://upload.url", Instant.now().plusSeconds(60))
            );

            AnnouncementResult result = TestAnnouncementData.result(savedId);

            when(mapper.toModel(command)).thenReturn(model);
            when(repository.save(any(Announcement.class))).thenReturn(saved);
            when(mapper.toMediaUploadRequest(saved, command)).thenReturn(mediaCommand);
            when(uploadMediaPort.generateUploadUrls(mediaCommand)).thenReturn(mediaResult);
            when(mapper.toResult(saved, mediaResult)).thenReturn(result);

            AnnouncementResult actual = useCase.execute(command);

            assertNotNull(actual);
            assertEquals(savedId, actual.id());

            verify(repository, times(1)).save(announcementCaptor.capture());
            Announcement toSave = announcementCaptor.getValue();
            assertEquals("Title", toSave.getTitle());
        }

        @Test
        void shouldReturnMediaResults_whenMediaUrlsGenerated() {
            UUID authUserId = UUID.randomUUID();
            UUID savedId = UUID.randomUUID();

            CreateAnnouncementCommand command = TestAnnouncementData.createCommand(authUserId);

            Announcement model = Announcement.builder()
                    .title(command.title())
                    .status(MediaStatus.ACTIVE)
                    .build();

            Announcement saved = TestAnnouncementData.existingAnnouncement(savedId);

            UploadMediaCommand mediaCommand = new UploadMediaCommand(
                    authUserId,
                    command.isAdmin(),
                    command.isPublic(),
                    savedId,
                    com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType.ANNOUNCEMENT,
                    java.util.Set.of()
            );

            UploadMediaResult m1 = new UploadMediaResult(
                    UUID.randomUUID(),
                    "announcements/key-1",
                    "https://upload.url/1",
                    Instant.now().plusSeconds(60)
            );
            UploadMediaResult m2 = new UploadMediaResult(
                    UUID.randomUUID(),
                    "announcements/key-2",
                    "https://upload.url/2",
                    Instant.now().plusSeconds(60)
            );
            Set<UploadMediaResult> mediaResults = Set.of(m1, m2);

            AnnouncementResult expected = AnnouncementResult.builder()
                    .id(savedId)
                    .title(saved.getTitle())
                    .status(saved.getStatus())
                    .uploadMediaResults(mediaResults)
                    .build();

            when(mapper.toModel(command)).thenReturn(model);
            when(repository.save(any(Announcement.class))).thenReturn(saved);
            when(mapper.toMediaUploadRequest(saved, command)).thenReturn(mediaCommand);
            when(uploadMediaPort.generateUploadUrls(mediaCommand)).thenReturn(mediaResults);
            when(mapper.toResult(saved, mediaResults)).thenReturn(expected);

            AnnouncementResult actual = useCase.execute(command);

            assertNotNull(actual);
            assertNotNull(actual.uploadMediaResults());
            assertEquals(2, actual.uploadMediaResults().size());
            assertTrue(actual.uploadMediaResults().contains(m1));
            assertTrue(actual.uploadMediaResults().contains(m2));

            verify(uploadMediaPort, times(1)).generateUploadUrls(mediaCommand);
            verify(mapper, times(1)).toResult(saved, mediaResults);
        }
    }

    @Nested
    class Validation {

        @Test
        void shouldThrowBadRequest_whenTitleIsBlank() {
            CreateAnnouncementCommand command = new CreateAnnouncementCommand(
                    UUID.randomUUID(),
                        true,
                    true,
                    "   ",
                    null,
                    null,
                    null,
                    null,
                    null
            );

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("announcement.title.blank", ex.getMessage());
        }
    }
}
