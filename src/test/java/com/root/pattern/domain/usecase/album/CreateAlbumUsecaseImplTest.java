package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.NewAlbumOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.adapter.repository.AlbumDataProviderImpl;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.CreateAlbumUsecase;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreateAlbumUsecaseImplTest {
    @Mock
    private AlbumDataProvider albumDataProvider;

    @Mock
    private MusicianDataProvider musicianDataProvider;

    private CreateAlbumUsecase sut;

    private Album albumBuilder(UUID id, String name) {
        return Album.builder()
            .id(id)
            .name(name)
            .build();
    }

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .name("any_name")
            .createdAt(new Date())
            .role(Role.MUSICIAN)
            .email("any_email")
            .updatedAt(new Date())
            .disabledAt(null)
            .disabled(false)
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = CreateAlbumUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();

        Musician musician = this.musicianBuilder();

        Mockito.when(this.musicianDataProvider.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(musician));
        Mockito.when(this.albumDataProvider.register(Mockito.any())).thenReturn(Album.builder()
            .id(UUID.randomUUID())
            .name("any_name")
            .createdAt(new Date())
            .musician(musician)
            .build()
        );
        Mockito.when(this.albumDataProvider.findByAlbumNameAndMusicianId(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
    }

    @Test
    public void shouldCallSutWithCorrectlyProvidedParams() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_album");

        CreateAlbumUsecase mocksut = Mockito.mock(CreateAlbumUsecaseImpl.class);
        mocksut.exec(1L, albumDto);

        Mockito.verify(mocksut, Mockito.times(1)).exec(1L, albumDto);
    }

    @Test
    public void shouldThrowExceptionIfMusicianIdIsEmpty() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_album");

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, albumDto);
        });

        Assertions.assertEquals("Musician Id can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfAlbumIsEmpty() {
        UUID albumId = UUID.randomUUID();

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(1L, null);
        });

        Assertions.assertEquals("Album can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfAlbumNameIsEmpty() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, null);

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(1L, albumDto);
        });

        Assertions.assertEquals("Album name can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianDontExists() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_name");

        Mockito.when(this.musicianDataProvider.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, albumDto);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianIsDisabled() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_name");

        Musician disabledMusician = this.musicianBuilder();
        disabledMusician.setDisabled(true);

        Mockito.when(this.musicianDataProvider.findById(Mockito.anyLong())).thenReturn(Optional.of(disabledMusician));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L, albumDto);
        });

        Assertions.assertEquals("Musician is disabled", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianAlreadyHasAlbumWithName() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_name");

        Mockito.when(this.albumDataProvider.findByAlbumNameAndMusicianId(albumDto.getName(), 1L)).thenReturn(Optional.of(
            albumDto
        ));

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(1L, albumDto);
        });

        Assertions.assertEquals("Musician already has an album with this name", exception.getMessage());
    }

    @Test
    public void shouldReturnOutputIfNothingGoesWrong() {
        UUID albumId = UUID.randomUUID();
        Album albumDto = this.albumBuilder(albumId, "any_name");

        NewAlbumOutputDTO sutOutput = this.sut.exec(1L, albumDto);

        Assertions.assertAll("Sut assertions",
            () -> Assertions.assertNotNull(sutOutput.getId()),
            () -> Assertions.assertEquals("any_name", sutOutput.getName()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt()),
            () -> Assertions.assertEquals("any_name", sutOutput.getMusicianName())
        );
    }
}