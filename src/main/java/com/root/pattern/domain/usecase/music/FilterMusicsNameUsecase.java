package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class FilterMusicsNameUsecase {
    private final MusicDataProvider musicDataProvider;

    public ListFilterMusicOutputDTO exec(String musicName, int page, int perPage) {
        this.validateInputs(musicName);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 100) {
            perPage = 100;
        }

        Page<Music> musicsList = this.getAllMusicsWithName(musicName, page, perPage);

        return this.mountOutput(musicsList, perPage);
    }

    public void validateInputs(String musicName) {
        if (Objects.isNull(musicName)) {
            throw new BadRequestException("Music name can't be empty");
        }
    }

    public Page<Music> getAllMusicsWithName(String name, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.DESC, "createdAt");

        return this.musicDataProvider.findAllByNameLike(pageable, name);
    }

    public ListFilterMusicOutputDTO mountOutput(Page<Music> musicList, int perPage) {
        return ListFilterMusicOutputDTO.builder()
            .page(musicList.getNumber() + 1)
            .perPage(perPage)
            .totalItems(musicList.getTotalElements())
            .musics(musicList.getContent().stream()
                .map(music -> FilterMusicOutputDTO.builder()
                    .id(music.getId())
                    .name(music.getName())
                    .duration(music.getDuration())
                    .category(CategoryOutputDTO.builder()
                        .id(music.getCategory().getId())
                        .name(music.getCategory().getName().toString())
                        .build()
                    )
                    .isSingle(music.getIsSingle())
                    .createdAt(music.getCreatedAt())
                    .album(Objects.nonNull(music.getAlbum()) ?
                        AlbumOutputDTO.builder()
                            .id(music.getAlbum().getId())
                            .name(music.getAlbum().getName())
                            .createdAt(music.getAlbum().getCreatedAt())
                            .totalMusics(music.getAlbum().getMusic().size())
                            .build() : null)
                    .musician(MusicianOutputDTO.builder()
                        .id(music.getMusician().getId())
                        .name(music.getMusician().getName())
                        .email(music.getMusician().getEmail())
                        .role(music.getMusician().getRole())
                        .createdAt(music.getMusician().getCreatedAt())
                        .build()
                    ).build()
                ).collect(Collectors.toList())
            ).build();
    }
}
