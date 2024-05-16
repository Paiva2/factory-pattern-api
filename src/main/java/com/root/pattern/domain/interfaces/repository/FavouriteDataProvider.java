package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Favourite;

import java.util.Optional;
import java.util.UUID;

public interface FavouriteDataProvider {
    Favourite create(Favourite favourite);

    Optional<Favourite> findByUserAndMusic(Long userId, UUID musicId);

    Integer getLastOrderOnUserFavourites(Long userId);

    void deleteById(UUID favouriteId);

    Optional<Favourite> findByUserAndId(Long userId, UUID id);

    void decreaseAllPositionsFromUser(Long userId, Integer position);
}
