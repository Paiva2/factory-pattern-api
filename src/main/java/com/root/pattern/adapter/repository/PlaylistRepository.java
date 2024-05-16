package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    @Query("SELECT p FROM Playlist p " +
        "JOIN FETCH p.user u " +
        "WHERE u.id = :userId " +
        "AND p.name = :playlistName OR p.name LIKE CONCAT('%', :playlistName, '%\\(\\d+\\)')" +
        "AND p.disabled IS NOT TRUE")
    Optional<Playlist> findActivesByName(@Param("playlistName") String playlistName, @Param("userId") Long userId);

    @Query(value = "SELECT DISTINCT(tbp.PLY_ID), tbp.* FROM TB_PLAYLISTS tbp " +
        "LEFT JOIN TB_PLAYLIST_MUSICS tpm ON tpm.PLM_PLAYLIST_ID = tbp.PLY_ID " +
        "WHERE tbp.PLY_USER_ID = :userId " +
        "AND tbp.PLY_DISABLED IS NOT TRUE " +
        "AND (:name IS NULL OR LOWER(tbp.PLY_NAME) LIKE CONCAT('%', LOWER(:name), '%')) " +
        "ORDER BY tbp.PLY_ORDER ASC " +
        "LIMIT :perPage OFFSET :offSet ",
        nativeQuery = true)
    List<Playlist> getAllActivePlaylistsFromUser(@Param("userId") Long userId, @Param("offSet") Integer offSet, @Param("perPage") Integer perPage, @Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM (SELECT DISTINCT(tbp.PLY_ID) FROM TB_PLAYLISTS tbp " +
        "LEFT JOIN TB_PLAYLIST_MUSICS tpm ON tpm.PLM_PLAYLIST_ID = tbp.PLY_ID " +
        "LEFT JOIN TB_MUSICS msc ON msc.MUS_ID = tpm.PLM_MUSIC_ID " +
        "WHERE tbp.PLY_USER_ID = :userId " +
        "AND tbp.PLY_DISABLED IS NOT TRUE " +
        "AND (:name IS NULL OR LOWER(tbp.PLY_NAME) LIKE CONCAT('%', LOWER(:name), '%')))",
        nativeQuery = true)
    Long countAllActivePlaylistsFromUser(@Param("userId") Long userId, @Param("name") String name);

    @Query(value = "SELECT p.PLY_ORDER FROM TB_PLAYLISTS p " +
        "JOIN TB_USERS u " +
        "ON u.U_ID = PLY_USER_ID WHERE u.U_ID = :userId " +
        "ORDER BY p.PLY_ORDER DESC " +
        "LIMIT 1", nativeQuery = true)
    Integer findLastOrderedByUser(@Param("userId") Long userId);

    @Query("SELECT p FROM Playlist p " +
        "LEFT JOIN FETCH p.playlistMusics pm " +
        "WHERE p.id = :playlistId " +
        "AND p.isPrivate IS NOT TRUE AND p.disabled IS NOT TRUE")
    Optional<Playlist> findByIdAndIsPublic(@Param("playlistId") UUID playlistId);
}
