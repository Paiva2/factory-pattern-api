package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterOneMusicUsecaseTest {
    @Mock
    private MusicDataProvider musicDataProvider;

    private FilterOneMusicUsecase sut;

    private Category categoryBuilder() {
        return Category.builder()
            .id(UUID.randomUUID())
            .name(MusicCategory.ROCK)
            .build();
    }

    private Album albumBuilder() {
        return Album.builder()
            .id(UUID.randomUUID())
            .name("any_name")
            .createdAt(new Date())
            .music(Collections.singletonList(Music.builder().build()))
            .build();
    }

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .name("any_name")
            .email("any_email")
            .role(Role.MUSICIAN)
            .createdAt(new Date())
            .build();
    }

    private Music musicBuilder(UUID id, String name, Category category, Album album, Musician musician, boolean isSingle) {
        return Music.builder()
            .id(id)
            .name(name)
            .createdAt(new Date())
            .updatedAt(new Date())
            .duration(2000L)
            .category(category)
            .album(album)
            .musician(musician)
            .isSingle(isSingle)
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = FilterOneMusicUsecase.builder()
            .musicDataProvider(this.musicDataProvider)
            .build();

        Musician musicianMock = this.musicianBuilder();
        Album albumMock = this.albumBuilder();
        Category categoryMock = this.categoryBuilder();
        Music musicMock = this.musicBuilder(UUID.randomUUID(), "any_name_1", categoryMock, albumMock, musicianMock, true);

        Mockito.when(this.musicDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(musicMock));
    }

    @Test
    public void shouldCallSutWithCorrectProvidedParams() {
        UUID musicId = UUID.randomUUID();
        FilterOneMusicUsecase mockSut = Mockito.mock(FilterOneMusicUsecase.class);
        mockSut.exec(musicId);

        Mockito.verify(mockSut, Mockito.times(1)).exec(musicId);
    }

    @Test
    public void shouldThrowExceptionIfMusicIdIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals("Music id can't be empty", exception.getMessage());
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        FilterMusicOutputDTO outputSut = this.sut.exec(UUID.randomUUID());

        Assertions.assertAll("Sut output assertions",
            () -> Assertions.assertNotNull(outputSut.getId()),
            () -> Assertions.assertEquals("any_name_1", outputSut.getName()),
            () -> Assertions.assertTrue(outputSut.getIsSingle()),
            () -> Assertions.assertEquals(2000L, outputSut.getDuration()),
            () -> Assertions.assertNotNull(outputSut.getCreatedAt()),
            () -> Assertions.assertNotNull(outputSut.getCategory().getId()),
            () -> Assertions.assertNotNull(outputSut.getMusician().getId()),
            () -> Assertions.assertNotNull(outputSut.getAlbum().getId())
        );
    }

}