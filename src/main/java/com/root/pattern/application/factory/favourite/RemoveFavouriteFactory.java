package com.root.pattern.application.factory.favourite;

import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.favourite.RemoveFavouriteUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class RemoveFavouriteFactory {
    private final UserDataProvider userDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    @Bean("RemoveFavouriteUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RemoveFavouriteUsecase create() {
        return RemoveFavouriteUsecase.builder()
            .userDataProvider(this.userDataProvider)
            .favouriteDataProvider(this.favouriteDataProvider)
            .build();
    }
}
