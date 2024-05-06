package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.UpdateAlbumUsecase;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.usecase.album.UpdateAlbumUsecaseImpl;
import lombok.Builder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Builder
public class UpdateAlbumFactory {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;

    @Bean("UpdateAlbumUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UpdateAlbumUsecase create() {
        return UpdateAlbumUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .albumDataProvider(this.albumDataProvider)
            .build();
    }
}
