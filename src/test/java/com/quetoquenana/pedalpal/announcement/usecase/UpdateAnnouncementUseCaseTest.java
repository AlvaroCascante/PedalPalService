package com.quetoquenana.pedalpal.announcement.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.util.TestAnnouncementData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAnnouncementUseCaseTest {

    @Mock
    AnnouncementRepository repository;

    @Mock
    AnnouncementMapper mapper;

    @InjectMocks
    UpdateAnnouncementUseCase useCase;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateTitle_whenProvided() {
            UUID id = UUID.randomUUID();
            UUID auth = UUID.randomUUID();

            Announcement existing = TestAnnouncementData.existingAnnouncement(id);
            when(repository.getById(id)).thenReturn(Optional.of(existing));

            Announcement saved = TestAnnouncementData.existingAnnouncement(id);
            when(repository.save(any(Announcement.class))).thenReturn(saved);

            AnnouncementResult result = TestAnnouncementData.result(id);
            when(mapper.toResult(saved)).thenReturn(result);

            UpdateAnnouncementCommand command = TestAnnouncementData.updateCommand_onlyTitle(id, auth);

            AnnouncementResult actual = useCase.execute(command);

            assertEquals(id, actual.id());
            verify(mapper, times(1)).applyPatch(existing, command);
            verify(repository, times(1)).save(existing);
        }
    }

    @Nested
    class ValidationAndGuards {

        @Test
        void shouldThrowNotFound_whenAnnouncementDoesNotExist() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.empty());

            UpdateAnnouncementCommand command = new UpdateAnnouncementCommand(
                    id,
                    UUID.randomUUID(),
                    "New title",
                    null,
                    null,
                    null,
                    null
            );

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(command));
            verify(repository, never()).save(any());
        }

        @Test
        void shouldThrowBadRequest_whenTitleIsBlank() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.of(TestAnnouncementData.existingAnnouncement(id)));

            UpdateAnnouncementCommand command = new UpdateAnnouncementCommand(
                    id,
                    UUID.randomUUID(),
                    "   ",
                    null,
                    null,
                    null,
                    null
            );

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
            assertEquals("announcement.update.title.blank", ex.getMessage());
            verify(repository, never()).save(any());
        }
    }
}
