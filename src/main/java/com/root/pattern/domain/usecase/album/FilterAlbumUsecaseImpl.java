package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.FilterAlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.usecase.album.FilterAlbumUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
public class FilterAlbumUsecaseImpl implements FilterAlbumUsecase {
    private final AlbumDataProvider albumDataProvider;

    @Override
    public FilterAlbumOutputDTO exec(UUID albumId) {
        this.validateInputs(albumId);

        Album album = this.checkIfAlbumExists(albumId);

        return this.mountOutput(album);
    }

    @Override
    public void validateInputs(UUID albumId) {
        if (Objects.isNull(albumId)) {
            throw new BadRequestException("Album id can't be empty");
        }
    }

    @Override
    public Album checkIfAlbumExists(UUID albumId) {
        return this.albumDataProvider.findById(albumId).orElseThrow(() -> new NotFoundException("Album"));
    }

    @Override
    public FilterAlbumOutputDTO mountOutput(Album album) {
        return FilterAlbumOutputDTO.builder()
            .id(album.getId())
            .name(album.getName())
            .createdAt(album.getCreatedAt())
            .updatedAt(album.getUpdatedAt())
            .musician(MusicianOutputDTO.builder()
                .id(album.getMusician().getId())
                .email(album.getMusician().getEmail())
                .name(album.getMusician().getName())
                .createdAt(album.getMusician().getCreatedAt())
                .role(album.getMusician().getRole())
                .build())
            .musics(album.getMusic().stream()
                .map(music -> MusicOutputDTO.builder()
                    .id(music.getId())
                    .name(music.getName())
                    .isSingle(music.isSingle())
                    .duration(music.getDuration())
                    .order(Math.toIntExact(music.getAlbumOrder()))
                    .category(CategoryOutputDTO.builder()
                        .id(music.getCategory().getId())
                        .name(music.getCategory().getName().toString())
                        .build())
                    .createdAt(music.getCreatedAt())
                    .build())
                .collect(Collectors.toList())
            ).build();
    }
}
