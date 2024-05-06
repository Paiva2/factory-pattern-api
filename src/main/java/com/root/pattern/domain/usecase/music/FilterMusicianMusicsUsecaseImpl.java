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
import com.root.pattern.domain.interfaces.usecase.FilterMusicianMusicsUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

//todo: tests
@Builder
@AllArgsConstructor
public class FilterMusicianMusicsUsecaseImpl implements FilterMusicianMusicsUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Override
    public ListFilterMusicOutputDTO exec(Long musicianId, Integer page, Integer perPage) {
        this.validateInputs(musicianId);
        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsNotDisabled(musician);

        Page<Music> musics = this.getAllMusicsFromMusician(musician.getId(), page, perPage);

        return this.mountOutput(musics);
    }

    @Override
    public void validateInputs(Long musicianId) {
        if (Objects.nonNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    @Override
    public void checkIfMusicianIsNotDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    @Override
    public Page<Music> getAllMusicsFromMusician(Long musicianId, Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.DESC);

        return this.musicDataProvider.findAllByMusician(pageable, musicianId);
    }

    @Override
    public ListFilterMusicOutputDTO mountOutput(Page<Music> musics) {
        return ListFilterMusicOutputDTO.builder()
            .page(musics.getNumber() + 1)
            .perPage(musics.getSize())
            .totalItems(musics.getTotalElements())
            .musics(musics.stream().map(music ->
                FilterMusicOutputDTO.builder()
                    .id(music.getId())
                    .name(music.getName())
                    .isSingle(music.isSingle())
                    .duration(music.getDuration())
                    .createdAt(music.getCreatedAt())
                    .category(CategoryOutputDTO.builder()
                        .id(music.getCategory().getId())
                        .name(music.getCategory().getName().name())
                        .build()
                    )
                    .musician(MusicianOutputDTO.builder()
                        .id(music.getMusician().getId())
                        .name(music.getMusician().getName())
                        .email(music.getMusician().getEmail())
                        .role(music.getMusician().getRole())
                        .createdAt(music.getMusician().getCreatedAt())
                        .build()
                    )
                    .album(AlbumOutputDTO.builder()
                        .id(music.getAlbum().getId())
                        .name(music.getAlbum().getName())
                        .createdAt(music.getAlbum().getCreatedAt())
                        .totalMusics(music.getAlbum().getMusic().size())
                        .build()
                    )
                    .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
