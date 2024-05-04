package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.NewAlbumOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.CreateAlbumUsecase;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.Optional;

// TODO: UNIT TESTS
@AllArgsConstructor
@Builder
public class CreateAlbumUsecaseImpl implements CreateAlbumUsecase {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;

    @Override
    public NewAlbumOutputDTO exec(Long musicianId, Album newAlbum) {
        this.inputValidations(musicianId, newAlbum);

        Musician musician = this.checkIfMusicianExists(musicianId);

        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }

        this.checkIfMusicianHasAlbum(newAlbum.getName(), musician.getId());

        newAlbum.setMusician(musician);

        Album albumCreated = this.albumDataProvider.register(newAlbum);

        return this.mountOutput(albumCreated);
    }

    @Override
    public void inputValidations(Long musicianId, Album newAlbum) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician Id can't be empty");
        }

        if (Objects.isNull(newAlbum)) {
            throw new BadRequestException("Album can't be empty");
        }

        if (Objects.isNull(newAlbum.getName())) {
            throw new BadRequestException("Album name can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(Long musicianId) {
        Musician musician = this.musicianDataProvider.findById(musicianId)
            .orElseThrow(() -> new NotFoundException("Musician"));

        return musician;
    }

    @Override
    public void checkIfMusicianHasAlbum(String albumName, Long musicianId) {
        Optional<Album> album = this.albumDataProvider.findByAlbumNameAndMusicianId(albumName, musicianId);

        if (album.isPresent()) {
            throw new ConflictException("Musician already has an album with this name");
        }
    }

    @Override
    public NewAlbumOutputDTO mountOutput(Album album) {
        return NewAlbumOutputDTO.builder()
            .id(album.getId())
            .name(album.getName())
            .createdAt(album.getCreatedAt())
            .musicianName(album.getMusician().getName())
            .build();
    }
}
