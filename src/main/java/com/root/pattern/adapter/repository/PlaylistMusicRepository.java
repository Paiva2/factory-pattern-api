package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, UUID> {
    @Modifying
    @Query(value = "UPDATE TB_PLAYLIST_MUSICS " +
        "SET PLM_DISABLED = TRUE, PLM_DISABLED_AT = now() " +
        "FROM TB_MUSICS " +
        "WHERE TB_MUSICS.MUS_ID = :musicId", nativeQuery = true)
    void disableAllByMusic(@Param("musicId") UUID musicId);
}
