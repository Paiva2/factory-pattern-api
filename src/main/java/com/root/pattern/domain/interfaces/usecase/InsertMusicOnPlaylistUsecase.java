package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.User;

import java.util.UUID;

public interface InsertMusicOnPlaylistUsecase {
    void exec(Long userId, UUID musicId, UUID playlistId);

    void validateInputs(Long userId, UUID musicId, UUID playlistId);

    User checkIfUserExists(Long userId);

    void checkIfUserIsDisabled(User user);

    Playlist checkIfPlaylistExists(UUID playlistId);

    void checkIfPlaylistIsDisabled(Playlist playlist);

    Music checkIfMusicExists(UUID musicId);

    void checkIfMusicIsDisabled(Music music);

    Long getLastMusicOrderOnPlaylist(UUID playlistId);

    void checkIfPlaylistBelongsToUser(User user, Playlist playlist);

    void checkIfPlaylistAlreadyHasMusic(UUID musicId, UUID playlistId);

    void insertMusicOnPlaylist(Music music, Playlist playlist);
}
