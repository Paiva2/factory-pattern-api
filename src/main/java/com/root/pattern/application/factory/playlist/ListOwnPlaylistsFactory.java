package com.root.pattern.application.factory.playlist;

import com.root.pattern.domain.interfaces.repository.PlaylistDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.interfaces.usecase.playlist.ListOwnPlaylistsUsecase;
import com.root.pattern.domain.usecase.playlist.ListOwnPlaylistsUsecaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class ListOwnPlaylistsFactory {
    private final UserDataProvider userDataProvider;
    private final PlaylistDataProvider playlistDataProvider;

    @Bean(value = "ListOwnPlaylistsUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ListOwnPlaylistsUsecase create() {
        return ListOwnPlaylistsUsecaseImpl.builder()
            .userDataProvider(this.userDataProvider)
            .playlistDataProvider(this.playlistDataProvider)
            .build();
    }
}
