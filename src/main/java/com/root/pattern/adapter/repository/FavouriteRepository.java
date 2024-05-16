package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, UUID> {

    Optional<Favourite> findByUserIdAndMusicId(Long userId, UUID musicId);

    Optional<Favourite> findByUserIdAndId(Long userId, UUID id);

    @Query(value = "SELECT fav.FAV_ORDER FROM TB_FAVOURITES fav " +
        "JOIN TB_USERS usr " +
        "ON usr.U_ID = fav.FAV_USER_ID " +
        "WHERE usr.U_ID = :userId " +
        "ORDER BY fav.FAV_ORDER DESC " +
        "LIMIT 1", nativeQuery = true)
    Integer findLastOrderOnUserFavourites(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE TB_FAVOURITES " +
        "SET FAV_ORDER = FAV_ORDER - 1 " +
        "WHERE FAV_USER_ID = :userId " +
        "AND FAV_ORDER > CAST(:position as integer)", nativeQuery = true)
    void decreaseAllFromUserFromPosition(
        @Param("userId") Long userId,
        @Param("position") Integer position
    );
}
