package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<Music, UUID> {
    @Query("SELECT m FROM Music m " +
        "JOIN FETCH m.album ma " +
        "WHERE m.name = :musicName " +
        "AND ma.id = :albumId " +
        "AND m.disabled IS NOT TRUE")
    Optional<Music> findByAlbumAndName(@Param("albumId") UUID albumId, @Param("musicName") String musicName);

    @Query(value = "SELECT m FROM Music m " +
        "LEFT JOIN m.album ma " +
        "WHERE (LOWER(m.name) LIKE CONCAT('%', LOWER(:musicName), '%') " +
        "AND m.disabled IS NOT TRUE)")
    Page<Music> findAllByNameLike(Pageable pageable, @Param("musicName") String musicName);

    @Modifying
    @Query("UPDATE Music m SET m.disabled = true, m.disabledAt = now() WHERE m.id IN (:ids)")
    void disableAll(@Param("ids") List<UUID> ids);

    @Query(value = "SELECT m FROM Music m " +
        "LEFT JOIN m.album ma " +
        "INNER JOIN m.musician msc " +
        "WHERE msc.id = :musicianId " +
        "AND m.disabled IS NOT TRUE")
    Page<Music> findAllByMusician(Pageable pageable, @Param("musicianId") Long musicianId);

    @Query(value = "SELECT msc.*, ctg.*, alb.*, ms.* FROM TB_MUSICS msc " +
        "JOIN TB_CATEGORIES ctg ON msc.MUS_CATEGORY_ID = ctg.CAT_ID " +
        "LEFT JOIN TB_ALBUMS alb ON msc.MUS_ALBUM_ID = alb.ALB_ID " +
        "JOIN TB_MUSICIANS ms ON msc.MUS_MUSICIAN_ID = ms.M_ID " +
        "WHERE ms.M_ID = :musicianId " +
        "AND msc.MUS_DISABLED IS NOT TRUE " +
        "AND alb.ALB_DISABLED IS NOT TRUE " +
        "AND (:name IS NULL OR LOWER(msc.MUS_NAME) LIKE CONCAT('%', LOWER(:name), '%')) " +
        "AND (:albumName IS NULL OR LOWER(alb.ALB_NAME) LIKE CONCAT('%', LOWER(:albumName), '%')) " +
        "ORDER BY msc.MUS_CREATED_AT DESC " +
        "LIMIT :perPage OFFSET :offSet "
        , nativeQuery = true)
    List<Music> findAllFromMusician(
        @Param("musicianId") Long musicianId,
        @Param("perPage") Integer perPage,
        @Param("offSet") Integer offSet,
        @Param("name") String name, @Param("albumName") String albumName
    );

    @Query(value = "SELECT COUNT(*) FROM TB_MUSICS msc " +
        "JOIN TB_CATEGORIES ctg ON msc.MUS_CATEGORY_ID = ctg.CAT_ID " +
        "LEFT JOIN TB_ALBUMS alb ON msc.MUS_ALBUM_ID = alb.ALB_ID " +
        "JOIN TB_MUSICIANS ms ON msc.MUS_MUSICIAN_ID = ms.M_ID " +
        "WHERE ms.M_ID = :musicianId " +
        "AND msc.MUS_DISABLED IS NOT TRUE " +
        "AND alb.ALB_DISABLED IS NOT TRUE " +
        "AND (:name IS NULL OR LOWER(msc.MUS_NAME) LIKE CONCAT('%', LOWER(:name), '%')) " +
        "AND (:albumName IS NULL OR LOWER(alb.ALB_NAME) LIKE CONCAT('%', LOWER(:albumName), '%')) "
        , nativeQuery = true)
    Long findAllFromMusicianCount(
        @Param("musicianId") Long musicianId,
        @Param("name") String name, @Param("albumName") String albumName
    );

    @Query(value = "SELECT msc.MUS_ALBUM_ORDER FROM TB_MUSICS msc " +
        "JOIN TB_ALBUMS alb " +
        "ON msc.MUS_ALBUM_ID = alb.ALB_ID " +
        "WHERE alb.ALB_MUSICIAN_ID = :musicianId " +
        "AND alb.ALB_ID = :albumId " +
        "ORDER BY msc.MUS_ALBUM_ORDER DESC " +
        "LIMIT 1", nativeQuery = true)
    Long findLastOrderedByAlbum(@Param("musicianId") Long musicianId, @Param("albumId") UUID albumId);
    
    @Query("SELECT m FROM Music m " +
        "JOIN FETCH m.album ma " +
        "WHERE m.id = :musicId " +
        "AND ma.id = :albumId " +
        "AND m.disabled IS NOT TRUE")
    Optional<Music> findByAlbumAndId(@Param("albumId") UUID albumId, @Param("musicId") UUID musicId);
}
