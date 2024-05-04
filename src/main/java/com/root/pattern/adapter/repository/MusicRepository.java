package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<Music, UUID> {
    @Query("SELECT m FROM Music m JOIN FETCH m.album ma WHERE m.name = :musicName AND ma.id = :albumId")
    Optional<Music> findByAlbumAndName(@Param("albumId") UUID albumId, @Param("musicName") String musicName);

    @Query(value = "SELECT m FROM Music m " +
        "LEFT JOIN m.album ma " +
        "WHERE (LOWER(m.name) LIKE CONCAT('%', LOWER(:musicName), '%') AND m.disabled IS NOT TRUE)")
    Page<Music> findAllByNameLike(Pageable pageable, @Param("musicName") String musicName);
}
