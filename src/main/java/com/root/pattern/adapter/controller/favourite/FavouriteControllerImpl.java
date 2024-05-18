package com.root.pattern.adapter.controller.favourite;

import com.root.pattern.adapter.dto.favourite.*;
import com.root.pattern.domain.usecase.favourite.ListOwnFavouritesUsecase;
import com.root.pattern.domain.usecase.favourite.NewFavouriteMusicUsecase;
import com.root.pattern.domain.usecase.favourite.RemoveFavouriteUsecase;
import com.root.pattern.domain.usecase.favourite.ReorderFavouriteUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class FavouriteControllerImpl implements FavouriteController {
    private final NewFavouriteMusicUsecase newFavouriteMusicUsecase;
    private final RemoveFavouriteUsecase removeFavouriteUsecase;
    private final ListOwnFavouritesUsecase listOwnFavouritesUsecase;
    private final ReorderFavouriteUsecase reorderFavouriteUsecase;

    @Override
    public ResponseEntity<NewFavouriteOutputDTO> create(
        Authentication authentication,
        @PathVariable("musicId") UUID musicId
    ) {
        Long userId = Long.valueOf(authentication.getName());
        NewFavouriteOutputDTO output = this.newFavouriteMusicUsecase.exec(userId, musicId);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<DeleteFavouriteOutputDTO> remove(
        Authentication authentication,
        @PathVariable("favouriteId") UUID favouriteId
    ) {
        Long userId = Long.valueOf(authentication.getName());
        DeleteFavouriteOutputDTO output = this.removeFavouriteUsecase.exec(userId, favouriteId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<ListOwnFavouritesDTO> list(
        Authentication authentication,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "size", required = false, defaultValue = "5") int perPage
    ) {
        Long userId = Long.valueOf(authentication.getName());
        ListOwnFavouritesDTO output = this.listOwnFavouritesUsecase.exec(userId, page, perPage);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<FavouriteOutputDTO> reorder(
        Authentication authentication,
        @PathVariable("favouriteId") UUID favouriteId,
        @RequestBody @Valid ReoderFavouriteInputDTO dto
    ) {
        Long userId = Long.valueOf(authentication.getName());
        FavouriteOutputDTO output = this.reorderFavouriteUsecase.exec(userId, favouriteId, dto.getNewOrder());

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
