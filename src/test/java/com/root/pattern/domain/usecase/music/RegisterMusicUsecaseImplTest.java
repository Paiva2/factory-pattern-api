package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.RegisterMusicUsecase;
import com.root.pattern.domain.interfaces.usecase.RegisterUserUsecase;
import com.root.pattern.domain.usecase.user.RegisterUserUsecaseImpl;
import lombok.AllArgsConstructor;
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
class RegisterMusicUsecaseImplTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private AlbumDataProvider albumDataProvider;

    @Mock
    private CategoryDataProvider categoryDataProvider;

    @Mock
    private MusicDataProvider musicDataProvider;

    private RegisterMusicUsecase sut;

    private Album albumBuilder() {
        return Album.builder().id(UUID.randomUUID()).createdAt(new Date()).build();
    }

    private Category categoryBuilder() {
        return Category.builder().name(MusicCategory.ROCK).id(UUID.randomUUID()).build();
    }

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .email("any_email")
            .role(Role.MUSICIAN)
            .password("any_password")
            .name("any_name")
            .createdAt(new Date())
            .updatedAt(new Date())
            .disabled(false)
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = RegisterMusicUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .albumDataProvider(this.albumDataProvider)
            .categoryDataProvider(this.categoryDataProvider)
            .build();

        Category mockCategory = this.categoryBuilder();
        Musician musician = this.musicianBuilder();
        Album album = this.albumBuilder();

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(musician));
        Mockito.when(this.categoryDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(mockCategory));
        Mockito.when(this.albumDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(album));
        Mockito.when(this.musicDataProvider.findByAlbumAndName(Mockito.any(), Mockito.any())).thenReturn(null);
    }

    @Test
    public void shoulCallSutWithCorrectlyProvidedParams() {
        RegisterMusicUsecase mockSut = Mockito.mock(RegisterMusicUsecaseImpl.class);
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(true).duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        mockSut.exec(1L, musicDto);

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L, musicDto);
    }

    @Test
    public void shouldThrowErrorIfMusicianIdIsNotProvided() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(true).duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, musicDto);
        });

        Assertions.assertEquals("Musician id can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(1L, null);
        });

        Assertions.assertEquals("Music can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsSingleAndAlbumIsProvided() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(true)
            .duration(2000L)
            .album(Album.builder().id(UUID.randomUUID()).build())
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Music can't have an album if it's a single", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsNotSingleAndAlbumIsNotProvided() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(false)
            .duration(2000L)
            .album(null)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Music must have an album if it's not a single", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicianDontExists() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(true)
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicCategoryDontExists() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(true)
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        Mockito.when(this.categoryDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Category not found", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsNotSingleAndAlbumDontExists() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(false)
            .album(Album.builder().id(UUID.randomUUID()).build())
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        Mockito.when(this.albumDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Album not found", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsNotSingleAndAlbumIsDisabled() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(false)
            .album(Album.builder().id(UUID.randomUUID()).build())
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        Album album = Album.builder().id(UUID.randomUUID()).disabled(true).disabledAt(new Date()).build();

        Mockito.when(this.albumDataProvider.findById(Mockito.any())).thenReturn(Optional.of(album));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Album disabled", exception.getMessage());
    }

    @Test
    public void shouldThrowErrorIfMusicIsNotSingleAndAlbumHasMusicWithSameName() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(false)
            .album(Album.builder().id(UUID.randomUUID()).build())
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).build())
            .build();

        Mockito.when(this.musicDataProvider.findByAlbumAndName(Mockito.any(), Mockito.any())).thenReturn(Optional.ofNullable(Music.builder().build()));

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () -> {
            this.sut.exec(1L, musicDto);
        });

        Assertions.assertEquals("Album already has an music with this title", exception.getMessage());
    }

    @Test
    public void shouldRegisterANewMusicAndReturnOutputIfNothingGoesWrong() {
        Music musicDto = Music.builder()
            .name("valid_name")
            .isSingle(false)
            .album(Album.builder().id(UUID.randomUUID()).build())
            .duration(2000L)
            .category(Category.builder().id(UUID.randomUUID()).name(MusicCategory.ROCK).build())
            .build();

        Music musicCreatedMock = Music.builder()
            .id(musicDto.getId())
            .isSingle(musicDto.isSingle())
            .album(musicDto.getAlbum())
            .duration(musicDto.getDuration())
            .category(musicDto.getCategory())
            .createdAt(new Date())
            .updatedAt(new Date())
            .disabled(false)
            .disabledAt(null)
            .build();

        Mockito.when(this.musicDataProvider.findByAlbumAndName(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(this.musicDataProvider.register(Mockito.any())).thenReturn(musicCreatedMock);

        NewMusicOutputDTO useCaseOutput = this.sut.exec(1L, musicDto);

        Assertions.assertAll("Usecase return assertions",
            () -> Assertions.assertEquals(musicCreatedMock.getId(), useCaseOutput.getMusicId()),
            () -> Assertions.assertEquals(musicCreatedMock.getName(), useCaseOutput.getName()),
            () -> Assertions.assertEquals(musicCreatedMock.getDuration(), useCaseOutput.getDuration()),
            () -> Assertions.assertEquals(musicCreatedMock.getAlbum().getName(), useCaseOutput.getAlbumName()),
            () -> Assertions.assertEquals(musicCreatedMock.getCategory().getName().toString(), useCaseOutput.getCategoryName()),
            () -> Assertions.assertEquals(musicCreatedMock.isSingle(), useCaseOutput.isSingle()),
            () -> Assertions.assertEquals(musicCreatedMock.getCreatedAt(), useCaseOutput.getCreatedAt())
        );
    }

}