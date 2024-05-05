package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.usecase.FilterMusicsNameUsecase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterMusicsNameUsecaseImplTest {
    @Mock
    private MusicDataProvider musicDataProvider;

    private FilterMusicsNameUsecase sut;

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
        this.sut = FilterMusicsNameUsecaseImpl.builder()
            .musicDataProvider(this.musicDataProvider)
            .build();


        Musician musicianMock = this.musicianBuilder();
        Album albumMock = this.albumBuilder();
        Category categoryMock = this.categoryBuilder();
        Music musicMockOne = this.musicBuilder(UUID.randomUUID(), "any_name_1", categoryMock, albumMock, musicianMock, true);
        Music musicMockTwo = this.musicBuilder(UUID.randomUUID(), "any_name_2", categoryMock, null, musicianMock, false);

        Mockito.when(this.musicDataProvider.findAllByNameLike(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(Arrays.asList(musicMockOne, musicMockTwo)));
    }

    @Test
    public void shouldCallSutWithCorrectProvidedParams() {
        FilterMusicsNameUsecase mockSut = Mockito.mock(FilterMusicsNameUsecaseImpl.class);
        mockSut.exec("any_name", 1, 5);

        Mockito.verify(mockSut, Mockito.times(1)).exec("any_name", 1, 5);
    }

    @Test
    public void shouldThrowExceptionIfMusicNameIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, 1, 5);
        });

        Assertions.assertEquals("Music name can't be empty", exception.getMessage());
    }

    @Test
    public void shouldCallGetAllMusiciansMethodWithPageOneIfPageZeroIsProvided() {
        int page = 0;
        int perPage = 5;

        this.sut.exec("any_name", page, perPage);

        Pageable pageable = PageRequest.of(0, perPage, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByNameLike(pageable, "any_name");
    }

    @Test
    public void shouldCallGetAllMusiciansMethodWithPerPageFiveIfLessIsProvided() {
        int page = 0;
        int perPage = 4;

        this.sut.exec("any_name", page, perPage);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByNameLike(pageable, "any_name");
    }

    @Test
    public void shouldCallGetAllMusiciansMethodWithPerPageOneHundredIfMoreIsProvided() {
        int page = 0;
        int perPage = 500;

        this.sut.exec("any_name", page, perPage);

        Pageable pageable = PageRequest.of(0, 100, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByNameLike(pageable, "any_name");
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        int page = 0;
        int perPage = 5;

        ListFilterMusicOutputDTO outputSut = this.sut.exec("any_name", page, perPage);

        Assertions.assertAll("Sut output assertions",
            () -> Assertions.assertEquals(1, outputSut.getPage()),
            () -> Assertions.assertEquals(5, outputSut.getPerPage()),
            () -> Assertions.assertEquals(2, outputSut.getTotalItems()),
            () -> Assertions.assertNotNull(outputSut.getMusics()),
            () -> Assertions.assertEquals("any_name_1", outputSut.getMusics().get(0).getName()),
            () -> Assertions.assertEquals("any_name_2", outputSut.getMusics().get(1).getName()),
            () -> Assertions.assertTrue(outputSut.getMusics().get(0).isSingle()),
            () -> Assertions.assertFalse(outputSut.getMusics().get(1).isSingle()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(0).getAlbum()),
            () -> Assertions.assertNull(outputSut.getMusics().get(1).getAlbum()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(0).getCategory()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getCategory()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(0).getMusician()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getMusician()),
            () -> Assertions.assertEquals("any_name", outputSut.getMusics().get(0).getMusician().getName()),
            () -> Assertions.assertEquals("any_name", outputSut.getMusics().get(1).getMusician().getName()),
            () -> Assertions.assertEquals("any_email", outputSut.getMusics().get(0).getMusician().getEmail()),
            () -> Assertions.assertEquals("any_email", outputSut.getMusics().get(1).getMusician().getEmail())
        );

    }

}