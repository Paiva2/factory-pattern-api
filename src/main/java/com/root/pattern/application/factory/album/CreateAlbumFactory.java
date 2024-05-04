package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.CreateAlbumUsecase;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.usecase.album.CreateAlbumUsecaseImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class CreateAlbumFactory {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;

    @Bean("CreateAlbumUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CreateAlbumUsecase create() {
        return CreateAlbumUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();
    }
}
