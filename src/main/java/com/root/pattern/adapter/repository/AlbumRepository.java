package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Album;
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
}
