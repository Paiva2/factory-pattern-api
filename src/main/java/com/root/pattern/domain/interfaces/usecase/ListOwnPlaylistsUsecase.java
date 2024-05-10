package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputdTO;
import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.entity.User;

import java.util.List;

public interface ListOwnPlaylistsUsecase {
    ListOwnPlaylistsOutputdTO exec(Long userId, Integer page, Integer perPage, String name);

    void validateInputs(Long userId);

    User checkIfUserExists(Long userId);

    void checkIfUserIsDisabled(User user);

    List<Playlist> getAllPlaylists(Long userId, String name, Integer page, Integer perPage);

    Long countPlaylists(Long userId, String name);

    ListOwnPlaylistsOutputdTO mountOutput(List<Playlist> playlists, Integer page, Integer perPage, Long totalItems);
}
