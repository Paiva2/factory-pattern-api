package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import com.root.pattern.domain.entity.Playlist;

public interface CreateNewPlaylistUsecasesStrategy {
    NewPlaylistOutputDTO exec(Long userId, Playlist playlist);

    void inputsValidation(Long userId, Playlist playlist);

    void checkIfUserHasSamePlaylist(String playlistName, Long userId);

    Playlist persistNewPlaylist(Playlist playlist);

    NewPlaylistOutputDTO mountOutput(Playlist playlist);
}
