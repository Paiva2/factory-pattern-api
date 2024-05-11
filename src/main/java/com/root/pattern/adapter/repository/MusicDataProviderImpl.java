package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Component
public class MusicDataProviderImpl implements MusicDataProvider {
    private final MusicRepository musicRepository;

    @Override
    public Optional<Music> findByAlbumAndName(UUID albumId, String musicName) {
        return this.musicRepository.findByAlbumAndName(albumId, musicName);
    }

    @Override
    public Music register(Music music) {
        return this.musicRepository.save(music);
    }

    @Override
    public Page<Music> findAllByNameLike(Pageable pageable, String name) {
        return this.musicRepository.findAllByNameLike(pageable, name);
    }

    @Override
    public Optional<Music> findById(UUID id) {
        return this.musicRepository.findById(id);
    }

    @Override
    public void disableAllWithId(List<UUID> ids) {
        this.musicRepository.disableAll(ids);
    }

    @Override
    public Page<Music> findAllByMusician(Pageable pageable, Long musicianId) {
        return this.musicRepository.findAllByMusician(pageable, musicianId);
    }

    @Override
    public List<Music> findAllFromMusician(Long musicianId, Integer perPage, Integer offSet, String name, String albumName) {
        return this.musicRepository.findAllFromMusician(musicianId, perPage, offSet, name, albumName);
    }

    @Override
    public Long findAllFromMusicianCount(Long musicianId, String name, String albumName) {
        return this.musicRepository.findAllFromMusicianCount(musicianId, name, albumName);
    }

    @Override
    public Long findLastMusicOnAlbumOrder(Long musicianId, UUID albumId) {
        return this.musicRepository.findLastOrderedByAlbum(musicianId, albumId);
    }
}
