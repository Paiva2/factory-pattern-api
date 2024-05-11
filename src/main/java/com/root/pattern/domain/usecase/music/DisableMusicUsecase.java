package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

//TODO: TESTS
@AllArgsConstructor
@Builder
public class DisableMusicUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final PlaylistMusicDataProvider playlistMusicDataProvider;

    @Transactional
    public MusicOutputDTO exec(Long musicianId, UUID musicId) {
        this.inputValidations(musicianId, musicId);

        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsDisabled(musician);

        Music music = this.checkIfMusicExists(musicId);
        this.checkIfMusicIsDisabled(music);
        this.checkIfMusicBelongsToMusician(music, musician);

        Music disabledMusic = this.disableMusic(music);

        return this.mountOutput(disabledMusic);
    }

    public void inputValidations(Long musicianId, UUID musicId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(musicId)) {
            throw new BadRequestException("Music id can't be empty");
        }
    }

    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    public Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    public void checkIfMusicIsDisabled(Music music) {
        if (music.isDisabled()) {
            throw new ConflictException("Music is already disabled");
        }
    }

    public void checkIfMusicBelongsToMusician(Music music, Musician musician) {
        Long musicianId = musician.getId();
        Long musicMusicianId = music.getMusician().getId();

        if (!musicMusicianId.equals(musicianId)) {
            throw new ForbiddenException("Music do not belongs to provided musician");
        }
    }

    public void disableAllPlaylistMusicsUsingMusic(Music music) {
        this.playlistMusicDataProvider.disableAllByMusicId(music.getId());
    }

    public Music disableMusic(Music music) {
        this.disableAllPlaylistMusicsUsingMusic(music);

        music.setDisabled(true);

        return this.musicDataProvider.register(music);
    }

    public MusicOutputDTO mountOutput(Music music) {
        return MusicOutputDTO.builder()
            .id(music.getId())
            .name(music.getName())
            .isSingle(music.isSingle())
            .duration(music.getDuration())
            .disabled(music.isDisabled())
            .order(Objects.nonNull(music.getAlbumOrder()) ? Math.toIntExact(music.getAlbumOrder()) : null)
            .category(CategoryOutputDTO.builder().id(music.getCategory().getId()).name(music.getCategory().getName().name()).build())
            .createdAt(music.getCreatedAt())
            .build();
    }
}
