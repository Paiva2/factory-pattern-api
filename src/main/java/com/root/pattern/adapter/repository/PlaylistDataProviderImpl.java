package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Playlist;
import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<Playlist> getAllActivePlaylists(Long userId, Integer offSet, Integer perPage, String name) {
        return this.playlistRepository.getAllActivePlaylistsFromUser(userId, offSet, perPage, name);
    }

    @Override
    public Long countActivePlaylists(Long userId, String name) {
        return this.playlistRepository.countAllActivePlaylistsFromUser(userId, name);
    }

    @Override
    public Integer getLastOrderedPlaylistFromUser(Long userId) {
        return this.playlistRepository.findLastOrderedByUser(userId);
    }

    @Override
    public Optional<Playlist> findById(UUID playlistId) {
        return this.playlistRepository.findByIdAndIsPublic(playlistId);
    }
}
