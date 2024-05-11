package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.musician.FilterMusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class FilterMusicianUsecase {
    private final MusicianDataProvider musicianDataProvider;

    public FilterMusicianOutputDTO exec(Long musicianId, String musicianName) {
        this.validateInputs(musicianId, musicianName);

        Musician musician = this.checkIfMusicianExists(musicianId, musicianName);

        this.checkIfMusicianIsDisabled(musician);

        return this.mountOutput(musician);
    }

    public void validateInputs(Long musicianId, String musicianName) {
        if (Objects.isNull(musicianId) && Objects.isNull(musicianName)) {
            throw new BadRequestException("Musician id and name can't be empty");
        }
    }

    public Musician checkIfMusicianExists(Long musicianId, String musicianName) {
        Musician musician = null;

        if (Objects.nonNull(musicianId) && Objects.nonNull(musicianName)) {
            musician = this.findMusicianId(musicianId);
        } else if (Objects.nonNull(musicianId)) {
            musician = this.findMusicianId(musicianId);
        } else if (Objects.nonNull(musicianName)) {
            musician = this.findMusicianName(musicianName);
        }

        return musician;
    }

    public Musician findMusicianId(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public Musician findMusicianName(String name) {
        return this.musicianDataProvider.findByName(name).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    public FilterMusicianOutputDTO mountOutput(Musician musician) {
        return FilterMusicianOutputDTO.builder()
            .id(musician.getId())
            .name(musician.getName())
            .email(musician.getEmail())
            .role(musician.getRole())
            .createdAt(musician.getCreatedAt())
            .albums(musician.getAlbums().stream()
                .map(album -> AlbumOutputDTO.builder()
                    .id(album.getId())
                    .name(album.getName())
                    .createdAt(album.getCreatedAt())
                    .totalMusics(album.getMusic().size())
                    .build())
                .collect(Collectors.toList())
            ).build();
    }
}
