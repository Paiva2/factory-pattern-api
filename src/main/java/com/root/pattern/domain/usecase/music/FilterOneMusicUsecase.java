package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class FilterOneMusicUsecase {
    private final MusicDataProvider musicDataProvider;

    public FilterMusicOutputDTO exec(UUID id) {
        this.validateInputs(id);
        Music music = this.checkIfMusicExists(id);

        return this.mountOutput(music);
    }

    public void validateInputs(UUID id) {
        if (Objects.isNull(id)) {
            throw new BadRequestException("Music id can't be empty");
        }
    }

    public Music checkIfMusicExists(UUID id) {
        return this.musicDataProvider.findById(id).orElseThrow(() -> new NotFoundException("Music"));
    }

    public FilterMusicOutputDTO mountOutput(Music music) {
        return FilterMusicOutputDTO.builder()
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
            .album(Objects.isNull(music.getAlbum()) ? null :
                AlbumOutputDTO.builder()
                    .id(music.getAlbum().getId())
                    .name(music.getAlbum().getName())
                    .createdAt(music.getAlbum().getCreatedAt())
                    .totalMusics(music.getAlbum().getMusic().size())
                    .build()
            )
            .build();
    }
}
