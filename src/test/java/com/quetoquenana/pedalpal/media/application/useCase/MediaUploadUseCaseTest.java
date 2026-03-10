package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.media.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.media.application.port.MediaOwnershipValidationPort;
import com.quetoquenana.pedalpal.media.application.port.MediaUrlProvider;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaContentType;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaUploadUseCaseTest {

    private static final String DEFAULT_PROVIDER = "r2";

    @Mock
    MediaRepository repository;

    @Mock
    MediaMapper mapper;

    @Mock
    MediaUrlProvider mediaUrlProvider;

    @Mock
    MediaOwnershipValidationPort mediaOwnershipValidationPort;

    @Mock
    AuthenticatedUserPort authenticatedUserPort;

    @Captor
    ArgumentCaptor<Media> mediaCaptor;

    MediaUploadUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new MediaUploadUseCase(
                repository,
                mapper,
                mediaUrlProvider,
                mediaOwnershipValidationPort,
                authenticatedUserPort,
                DEFAULT_PROVIDER
        );
    }

    @Test
    void shouldGenerateUploadUrlsAndPersistMedia() {
        UUID authUserId = UUID.randomUUID();
        UUID referenceId = UUID.randomUUID();

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(authUserId, "test-user", "Test User", UserType.CUSTOMER)));

        UploadMediaSpecCommand spec1 = new UploadMediaSpecCommand(
                "image",
                "image/jpeg",
                true,
                "front",
                "Front view"
        );
        UploadMediaSpecCommand spec2 = new UploadMediaSpecCommand(
                "image",
                "image/png",
                false,
                "side",
                "Side view"
        );

        UploadMediaCommand command = new UploadMediaCommand(
                false,
                referenceId,
                MediaReferenceType.BIKE,
                Set.of(spec1, spec2)
        );

        Media media1 = Media.builder()
                .id(UUID.randomUUID())
                .storageKey("media/key-1")
                .contentType(MediaContentType.IMAGE_JPEG)
                .status(MediaStatus.DRAFT)
                .build();
        Media media2 = Media.builder()
                .id(UUID.randomUUID())
                .storageKey("media/key-2")
                .contentType(MediaContentType.IMAGE_PNG)
                .status(MediaStatus.DRAFT)
                .build();

        SignedUrl signedUrl1 = new SignedUrl("https://upload/1", Instant.now().plusSeconds(60), Map.of("Content-Type", "image/jpeg"));
        SignedUrl signedUrl2 = new SignedUrl("https://upload/2", Instant.now().plusSeconds(60), Map.of("Content-Type", "image/png"));

        MediaResult result1 = new MediaResult(
                media1.getId(),
                media1.getContentType().name(),
                DEFAULT_PROVIDER,
                true,
                MediaStatus.DRAFT,
                "front",
                "Front view",
                signedUrl1.url(),
                signedUrl1.expiresAt()
        );
        MediaResult result2 = new MediaResult(
                media2.getId(),
                media2.getContentType().name(),
                DEFAULT_PROVIDER,
                false,
                MediaStatus.DRAFT,
                "side",
                "Side view",
                signedUrl2.url(),
                signedUrl2.expiresAt()
        );

        when(mapper.toModel(eq(authUserId), eq(command), eq(spec1))).thenReturn(media1);
        when(mapper.toModel(eq(authUserId), eq(command), eq(spec2))).thenReturn(media2);
        when(mediaUrlProvider.generateUploadUrl(media1.getStorageKey(), media1.getContentType().getContentType(), false)).thenReturn(signedUrl1);
        when(mediaUrlProvider.generateUploadUrl(media2.getStorageKey(), media2.getContentType().getContentType(), false)).thenReturn(signedUrl2);
        when(mapper.toResult(any(Media.class), eq(signedUrl1))).thenReturn(result1);
        when(mapper.toResult(any(Media.class), eq(signedUrl2))).thenReturn(result2);

        List<MediaResult> results = useCase.execute(command);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.containsAll(List.of(result1, result2)));

        verify(mediaOwnershipValidationPort, times(1)).validate(
                MediaReferenceType.BIKE,
                referenceId
        );
        verify(mediaUrlProvider, times(1)).generateUploadUrl(media1.getStorageKey(), media1.getContentType().getContentType(), false);
        verify(mediaUrlProvider, times(1)).generateUploadUrl(media2.getStorageKey(), media2.getContentType().getContentType(), false);
        verify(repository, times(2)).save(mediaCaptor.capture());

        List<Media> persisted = mediaCaptor.getAllValues();
        assertEquals(2, persisted.size());
        assertTrue(persisted.stream().allMatch(media -> DEFAULT_PROVIDER.equals(media.getProvider())));
    }
}
