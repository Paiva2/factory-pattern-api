package com.root.pattern.application.factory.favourite;

import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.favourite.NewFavouriteMusicUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class NewFavouriteFactory {
    private final UserDataProvider userDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    @Bean("NewFavouriteMusicUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public NewFavouriteMusicUsecase create() {
        return NewFavouriteMusicUsecase.builder()
            .userDataProvider(this.userDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .favouriteDataProvider(this.favouriteDataProvider)
            .build();
    }
}
