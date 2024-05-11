package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.music.GetAllOwnMusicsUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: TESTS
@AllArgsConstructor
@Builder
public class GetAllOwnMusicsUsecaseImpl implements GetAllOwnMusicsUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Override
    public ListFilterMusicOutputDTO exec(Long musicianId, Integer page, Integer perPage, String name, String albumName) {
        this.validateInputs(musicianId);

        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsDisabled(musician);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        List<Music> musics = this.getAllMusics(musician.getId(), page, perPage, name, albumName);
        Long musicsCount = this.getAllMusicsCount(musicianId, name, albumName);

        return this.mountOutput(musics, page, perPage, musicsCount);
    }

    @Override
    public void validateInputs(Long musicianId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    @Override
    public void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    @Override
    public List<Music> getAllMusics(Long musicianId, Integer page, Integer perPage, String name, String albumName) {
        return this.musicDataProvider.findAllFromMusician(musicianId, perPage, perPage * (page - 1), name, albumName);
    }

    @Override
    public Long getAllMusicsCount(Long musicianId, String name, String albumName) {
        return this.musicDataProvider.findAllFromMusicianCount(musicianId, name, albumName);
    }

    @Override
    public ListFilterMusicOutputDTO mountOutput(List<Music> musics, Integer page, Integer perPage, Long totalItems) {
        return ListFilterMusicOutputDTO.builder()
            .page(page + 1)
            .perPage(perPage)
            .totalItems(totalItems)
            .musics(musics.stream().map(music ->
                FilterMusicOutputDTO.builder()
                    .id(music.getId())
                    .name(music.getName())
                    .duration(music.getDuration())
                    .isSingle(music.isSingle())
                    .createdAt(music.getCreatedAt())
                    .category(CategoryOutputDTO.builder()
                        .id(music.getCategory().getId())
                        .name(music.getCategory().getName().name())
                        .build()
                    )
                    .album(Objects.nonNull(music.getAlbum()) ?
                        AlbumOutputDTO.builder()
                            .id(music.getAlbum().getId())
                            .name(music.getAlbum().getName())
                            .createdAt(music.getAlbum().getCreatedAt())
                            .totalMusics(music.getAlbum().getMusic().size())
                            .build()
                        : null
                    )
                    .musician(MusicianOutputDTO.builder()
                        .id(music.getMusician().getId())
                        .name(music.getMusician().getName())
                        .email(music.getMusician().getEmail())
                        .createdAt(music.getMusician().getCreatedAt())
                        .role(music.getMusician().getRole())
                        .build()
                    )
                    .build()

            ).collect(Collectors.toList()))
            .build();
    }
}
