package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Favourite;
import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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

    @Override
    public void deleteById(UUID favouriteId) {
        this.favouriteRepository.deleteById(favouriteId);
    }

    @Override
    public Optional<Favourite> findByUserAndId(Long userId, UUID id) {
        return this.favouriteRepository.findByUserIdAndId(userId, id);
    }

    @Override
    public void decreaseAllPositionsFromUser(Long userId, Integer position) {
        this.favouriteRepository.decreaseAllFromUserFromPosition(userId, position);
    }

    @Override
    public Page<Favourite> findAllByUser(Long userId, Pageable pageable) {
        return this.favouriteRepository.findAllByUserId(userId, pageable);
    }
}
