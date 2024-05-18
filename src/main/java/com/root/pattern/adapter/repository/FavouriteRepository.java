package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Favourite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT f FROM Favourite f " +
        "JOIN FETCH User u ON u.id = f.user.id " +
        "JOIN FETCH Music m ON m.id = f.music.id " +
        "WHERE u.id = :userId")
    Page<Favourite> findAllByUserId(
        @Param("userId") Long userId,
        Pageable pageable
    );

    @Modifying
    @Query("UPDATE Favourite f " +
        "SET f.favouriteOrder = f.favouriteOrder + 1 " +
        "WHERE f.user.id = :userId " +
        "AND f.favouriteOrder BETWEEN CAST(:newOrder as integer) AND CAST(:oldOrder as integer)")
    void reorderListByUserBetween(
        @Param("userId") Long userId,
        @Param("newOrder") Integer newOrder,
        @Param("oldOrder") Integer oldOrder
    );
}
