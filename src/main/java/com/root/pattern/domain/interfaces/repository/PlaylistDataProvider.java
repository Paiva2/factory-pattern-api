package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Playlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistDataProvider {
    Optional<Playlist> findUserActivesByName(String playlistName, Long userId);

    Playlist create(Playlist playlist);

    List<Playlist> getAllActivePlaylists(Long userId, Integer offSet, Integer perPage, String name);

    Long countActivePlaylists(Long userId, String name);

    Integer getLastOrderedPlaylistFromUser(Long userId);

    Optional<Playlist> findById(UUID playlistId);
}
