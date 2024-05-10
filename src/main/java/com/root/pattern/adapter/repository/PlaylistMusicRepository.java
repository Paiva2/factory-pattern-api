package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, UUID> {
}
