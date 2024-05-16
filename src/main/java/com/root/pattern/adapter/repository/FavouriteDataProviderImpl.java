package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Favourite;
import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FavouriteDataProviderImpl implements FavouriteDataProvider {
    private final FavouriteRepository favouriteRepository;

    @Override
    public Favourite create(Favourite favourite) {
        return this.favouriteRepository.save(favourite);
    }

    @Override
    public Optional<Favourite> findByUserAndMusic(Long userId, UUID musicId) {
        return this.favouriteRepository.findByUserIdAndMusicId(userId, musicId);
    }

    @Override
    public Integer getLastOrderOnUserFavourites(Long userId) {
        return this.favouriteRepository.findLastOrderOnUserFavourites(userId);
    }
}
