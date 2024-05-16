package com.root.pattern.domain.usecase.favourite;

import com.root.pattern.adapter.dto.favourite.DeleteFavouriteOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Favourite;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
public class RemoveFavouriteUsecase {
    private final UserDataProvider userDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    @Transactional
    public DeleteFavouriteOutputDTO exec(Long userId, UUID favouriteId) {
        this.validateInputs(userId, favouriteId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Favourite favourite = this.checkIfFavouriteExists(user.getId(), favouriteId);
        this.checkIfFavouriteBelongToUser(user.getId(), favourite);

        this.decreaseAllPositionsOnUserFavourite(user.getId(), favourite.getFavouriteOrder());

        this.deleteFavourite(favourite.getId());

        return this.mountOutput(favourite);
    }

    private void validateInputs(Long userId, UUID favouriteId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(favouriteId)) {
            throw new BadRequestException("Favourite id can't be empty");
        }
    }

    private User checkIfUserExists(Long userId) {
        return this.userDataProvider.findById(userId).orElseThrow(() -> new NotFoundException("User"));
    }

    private void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    private Favourite checkIfFavouriteExists(Long userId, UUID id) {
        return this.favouriteDataProvider.findByUserAndId(userId, id).orElseThrow(() -> new NotFoundException("Favourite"));
    }

    private void checkIfFavouriteBelongToUser(Long userId, Favourite favourite) {
        Long favouriteUserId = favourite.getUser().getId();

        if (!userId.equals(favouriteUserId)) {
            throw new ForbiddenException("User do not owns this favourited music");
        }
    }

    private void decreaseAllPositionsOnUserFavourite(Long userId, Integer position) {
        this.favouriteDataProvider.decreaseAllPositionsFromUser(userId, position);
    }

    private void deleteFavourite(UUID favouriteId) {
        this.favouriteDataProvider.deleteById(favouriteId);
    }

    private DeleteFavouriteOutputDTO mountOutput(Favourite favourite) {
        return DeleteFavouriteOutputDTO.builder()
            .id(favourite.getId())
            .build();
    }
}
