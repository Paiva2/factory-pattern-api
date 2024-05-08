package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PlaylistDataProviderImpl implements PlaylistDataProvider {
    private final PlaylistRepository playlistRepository;

    @Override
    public Optional<Playlist> findUserActivesByName(String playlistName, Long userId) {
        return this.playlistRepository.findActivesByName(playlistName, userId);
    }

    @Override
    public Playlist create(Playlist playlist) {
        return this.playlistRepository.save(playlist);
    }
}
