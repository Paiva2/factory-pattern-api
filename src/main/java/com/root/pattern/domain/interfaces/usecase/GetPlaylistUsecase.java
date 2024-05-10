package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.domain.entity.Playlist;

import java.util.UUID;

public interface GetPlaylistUsecase {
    GetPlaylistOutputDTO exec(UUID playlistId);

    void validateInputs(UUID playlistId);

    Playlist checkIfPlaylistExists(UUID playlistId);

    void checkIfPlaylistIsDisabled(Playlist playlist);

    GetPlaylistOutputDTO mountOutput(Playlist playlist);
}
