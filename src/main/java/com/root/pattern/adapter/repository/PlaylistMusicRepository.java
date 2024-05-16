package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, UUID> {
    @Modifying
    @Query(value = "UPDATE TB_PLAYLIST_MUSICS " +
        "SET PLM_DISABLED = TRUE, PLM_DISABLED_AT = now() " +
        "FROM TB_MUSICS " +
        "WHERE TB_MUSICS.MUS_ID = :musicId", nativeQuery = true)
    void disableAllByMusic(@Param("musicId") UUID musicId);

    @Query(value = "SELECT pm.PLM_MUSIC_PLAYLIST_ORDER FROM TB_PLAYLIST_MUSICS pm " +
        "WHERE pm.PLM_PLAYLIST_ID = :playlistId " +
        "ORDER BY pm.PLM_MUSIC_PLAYLIST_ORDER DESC " +
        "LIMIT 1", nativeQuery = true)
    Long findLastMusicOrder(@Param("playlistId") UUID playlistId);

    @Query("SELECT pm FROM PlaylistMusic pm " +
        "JOIN FETCH pm.playlist pl " +
        "JOIN FETCH pm.music msc " +
        "WHERE msc.id = :musicId AND pl.id = :playlistId")
    Optional<PlaylistMusic> findByPlaylistAndMusic(@Param("playlistId") UUID playlistId, @Param("musicId") UUID musicId);

    @Modifying
    @Query("UPDATE PlaylistMusic pm SET pm.musicPlaylistOrder = pm.musicPlaylistOrder - 1 " +
        "WHERE pm.playlist.id = :playlistId " +
        "AND pm.musicPlaylistOrder > CAST(:positionBeingRemoved as integer)")
    void decreaseAllOrderFromMusicOnPlaylist(
        @Param("playlistId") UUID playlistId,
        @Param("positionBeingRemoved") Integer positionBeingRemoved
    );

    @Modifying
    @Query("UPDATE PlaylistMusic pm SET pm.musicPlaylistOrder = pm.musicPlaylistOrder + 1 " +
        "WHERE pm.playlist.id = :playlistId " +
        "AND pm.musicPlaylistOrder BETWEEN CAST(:positionFrom as integer) AND CAST(:oldPosition as integer)")
    void reorderAllOrderFromMusicOnPlaylist(
        @Param("playlistId") UUID playlistId,
        @Param("positionFrom") Integer positionFrom,
        @Param("oldPosition") Integer oldPosition
    );

    @Query("SELECT pm FROM PlaylistMusic pm JOIN FETCH pm.playlist pl WHERE pl.id = :playlistId")
    Set<PlaylistMusic> findAllByPlaylist(@Param("playlistId") UUID playlistId);
}
