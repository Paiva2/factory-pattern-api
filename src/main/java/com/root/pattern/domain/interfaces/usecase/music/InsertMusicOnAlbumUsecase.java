package com.root.pattern.domain.interfaces.usecase.music;

import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;

import java.util.UUID;

public interface InsertMusicOnAlbumUsecase {
    MusicOutputDTO exec(Long musicianId, UUID albumId, UUID musicId);

    void validateInputs(Long musicianId, UUID albumId, UUID musicId);

    Musician checkIfMusicianExists(Long musicianId);

    void checkIfMusicianIsDisabled(Musician musician);

    Album checkIfAlbumExists(UUID albumId);

    void checkIfAlbumIsDisabled(Album album);

    void checkIfAlbumBelongsToMusician(Album album, Musician musician);

    Music checkIfMusicExists(UUID musicId);

    void checkIfMusicIsDisabled(Music music);

    void checkIfMusicAlreadyHasAlbum(Music music);

    void checkIfAlbumAlreadyHasMusic(Music music, Album album);

    Long getAlbumLastMusicOrder(Long musicianId, UUID albumId);

    Music insertMusicAlbum(Music music, Album album);

    MusicOutputDTO mountOutput(Music music);
}
