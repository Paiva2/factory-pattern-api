package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Playlist;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface PlaylistDataProvider {
    Optional<Playlist> findUserActivesByName(String playlistName, Long userId);

    Playlist create(Playlist playlist);
}
