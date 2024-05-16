package com.root.pattern.adapter.controller.favourite;

import com.root.pattern.adapter.dto.favourite.NewFavouriteOutputDTO;
import com.root.pattern.domain.usecase.favourite.NewFavouriteMusicUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class FavouriteControllerImpl implements FavouriteController {
    private final NewFavouriteMusicUsecase newFavouriteMusicUsecase;

    @Override
    public ResponseEntity<NewFavouriteOutputDTO> create(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId
    ) {
        Long userId = Long.valueOf(authentication.getName());
        NewFavouriteOutputDTO output = this.newFavouriteMusicUsecase.exec(userId, musicId);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
