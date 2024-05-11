package com.root.pattern.domain.interfaces.usecase.music;

import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;

import java.util.UUID;

public interface DisableMusicUsecase {
    MusicOutputDTO exec(Long musicianId, UUID musicId);

    void inputValidations(Long musicianId, UUID musicId);

    Musician checkIfMusicianExists(Long musicianId);

    void checkIfMusicianIsDisabled(Musician musician);

    Music checkIfMusicExists(UUID musicId);

    void checkIfMusicIsDisabled(Music music);

    void checkIfMusicBelongsToMusician(Music music, Musician musician);

    void disableAllPlaylistMusicsUsingMusic(Music music);

    Music disableMusic(Music music);

    MusicOutputDTO mountOutput(Music music);
}
