package com.root.pattern.application.factory.album;

import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.usecase.album.FilterAlbumsNameUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class FilterAlbumsNameFactory {
    private final AlbumDataProvider albumDataProvider;

    @Bean("FilterAlbumsNameUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterAlbumsNameUsecase create() {
        return FilterAlbumsNameUsecase.builder()
            .albumDataProvider(this.albumDataProvider)
            .build();
    }
}
