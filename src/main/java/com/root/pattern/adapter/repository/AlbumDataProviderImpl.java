package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Component
public class AlbumDataProviderImpl implements AlbumDataProvider {
    private final AlbumRepository albumRepository;

    @Override
    public Optional<Album> findById(UUID id) {
        return this.albumRepository.findById(id);
    }

    @Override
    public Album register(Album album) {
        return this.albumRepository.save(album);
    }

    @Override
    public Optional<Album> findByAlbumNameAndMusicianId(String albumName, Long musicianId) {
        return this.albumRepository.findByAlbumNameAndMusicianId(albumName, musicianId);
    }

    @Override
    public Page<Album> findAllByMusicianId(Pageable pageable, Long musicianId) {
        return this.albumRepository.findAllByMusicianId(pageable, musicianId);
    }

    @Override
    public Page<Album> findAllByMusicianIdAndAlbumName(Pageable pageable, Long musicianId, String albumName) {
        return this.albumRepository.findAllByMusicianIdAndAlbumName(pageable, musicianId, albumName);
    }

    @Override
    public Page<Album> findAllByNameLike(Pageable pageable, String name) {
        return this.albumRepository.findAllByNameLike(pageable, name);
    }

    @Override
    public Album disable(Album album) {
        return this.albumRepository.save(album);
    }
}
