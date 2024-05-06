package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
    @Query("SELECT alb FROM Album alb JOIN FETCH alb.musician ms WHERE alb.name = :albumName AND ms.id = :musicianId")
    Optional<Album> findByAlbumNameAndMusicianId(@Param("albumName") String albumName, @Param("musicianId") Long musicianId);

    @Query(value = "SELECT alb FROM Album alb " +
        "JOIN FETCH alb.musician ms " +
        "LEFT JOIN FETCH alb.music msc " +
        "WHERE ms.id = :musicianId " +
        "AND alb.disabled IS NOT TRUE",
        countQuery = "SELECT count(alb) FROM Album alb " +
            "JOIN alb.musician ms " +
            "LEFT JOIN alb.music msc " +
            "WHERE ms.id = :musicianId " +
            "AND alb.disabled IS NOT TRUE")
    Page<Album> findAllByMusicianId(Pageable pageable, @Param("musicianId") Long musicianId);

    @Query(value = "SELECT alb FROM Album alb " +
        "JOIN FETCH alb.musician ms " +
        "LEFT JOIN FETCH alb.music msc " +
        "WHERE ms.id = :musicianId " +
        "AND LOWER(alb.name) LIKE CONCAT('%', LOWER(:albumName), '%') " +
        "AND alb.disabled IS NOT TRUE",
        countQuery = "SELECT count(alb) FROM Album alb " +
            "JOIN alb.musician ms " +
            "LEFT JOIN alb.music msc " +
            "WHERE ms.id = :musicianId " +
            "AND LOWER(alb.name) LIKE CONCAT('%', LOWER(:albumName), '%') " +
            "AND alb.disabled IS NOT TRUE")
    Page<Album> findAllByMusicianIdAndAlbumName(Pageable pageable, @Param("musicianId") Long musicianId, @Param("albumName") String albumName);

    @Query(value = "SELECT alb FROM Album alb " +
        "JOIN FETCH alb.musician ms " +
        "LEFT JOIN FETCH alb.music msc " +
        "WHERE LOWER(alb.name) LIKE CONCAT('%', LOWER(:albumName), '%') " +
        "AND alb.disabled IS NOT TRUE",
        countQuery = "SELECT count(alb) FROM Album alb " +
            "JOIN alb.musician ms " +
            "LEFT JOIN alb.music msc " +
            "WHERE LOWER(alb.name) LIKE CONCAT('%', LOWER(:albumName), '%') " +
            "AND alb.disabled IS NOT TRUE")
    Page<Album> findAllByNameLike(Pageable pageable, @Param("albumName") String name);
}
