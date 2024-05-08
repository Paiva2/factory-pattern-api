package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    @Query("SELECT p FROM Playlist p " +
        "JOIN FETCH p.user u " +
        "WHERE u.id = :userId " +
        "AND p.name = :playlistName " +
        "AND p.disabled IS NOT TRUE")
    Optional<Playlist> findActivesByName(@Param("playlistName") String playlistName, @Param("userId") Long userId);
}
