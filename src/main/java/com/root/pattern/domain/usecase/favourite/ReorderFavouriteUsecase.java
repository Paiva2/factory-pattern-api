package com.root.pattern.domain.usecase.favourite;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.favourite.FavouriteOutputDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
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
public class ReorderFavouriteUsecase {
    private final UserDataProvider userDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    @Transactional
    public FavouriteOutputDTO exec(Long userId, UUID favouriteId, Integer newOrder) {
        this.validateInputs(userId, favouriteId, newOrder);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Favourite favourite = this.checkIfFavouriteExists(favouriteId);
        this.checkIfUserOwnsFavourite(user, favourite);

        this.validateFavouriteNewOrder(newOrder, user);
        this.reorderAllPositionsOnFavouritedList(user, newOrder, favourite.getFavouriteOrder());

        favourite.setFavouriteOrder(newOrder);
        Favourite favouriteUpdated = this.updateFavouritePosition(favourite);

        return this.mountOutput(favouriteUpdated);
    }

    private void validateInputs(Long userId, UUID favouriteId, Integer newOrder) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
        }

        if (Objects.isNull(favouriteId)) {
            throw new BadRequestException("Favourite id can't be empty");
        }

        if (Objects.isNull(newOrder) || newOrder < 0) {
            throw new BadRequestException("New order can't be empty or less than 0");
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

    private Favourite checkIfFavouriteExists(UUID favouriteId) {
        return this.favouriteDataProvider.findById(favouriteId).orElseThrow(() -> new NotFoundException("Favourite"));
    }

    private void checkIfUserOwnsFavourite(User user, Favourite favourite) {
        Long userId = user.getId();
        Long favouriteUserId = favourite.getUser().getId();

        if (!userId.equals(favouriteUserId)) {
            throw new ForbiddenException("User do not own favourite");
        }
    }

    private Integer getLastFavouritedPosition(Long userId) {
        return this.favouriteDataProvider.getLastOrderOnUserFavourites(userId);
    }

    private void validateFavouriteNewOrder(Integer newOrder, User user) {
        Integer maxPositionAvailable = this.getLastFavouritedPosition(user.getId());

        if (newOrder > maxPositionAvailable) {
            throw new ConflictException("New order can't be higher than last favourited music on list");
        }
    }

    private void reorderAllPositionsOnFavouritedList(User user, Integer newOrder, Integer oldOrder) {
        this.favouriteDataProvider.reorderAllPositionsByUserBetween(user.getId(), newOrder, oldOrder);
    }

    private Favourite updateFavouritePosition(Favourite favourite) {
        return this.favouriteDataProvider.create(favourite);
    }

    private FavouriteOutputDTO mountOutput(Favourite favourite) {
        return FavouriteOutputDTO.builder()
            .id(favourite.getId())
            .favouriteOrder(favourite.getFavouriteOrder())
            .createdAt(favourite.getCreatedAt())
            .music(FilterMusicOutputDTO.builder()
                .id(favourite.getMusic().getId())
                .name(favourite.getMusic().getName())
                .createdAt(favourite.getMusic().getCreatedAt())
                .duration(favourite.getMusic().getDuration())
                .isSingle(favourite.getMusic().getIsSingle())
                .category(CategoryOutputDTO.builder()
                    .id(favourite.getMusic().getCategory().getId())
                    .name(favourite.getMusic().getCategory().getName().name())
                    .build()
                )
                .album(Objects.isNull(favourite.getMusic().getAlbum()) ? null :
                    AlbumOutputDTO.builder()
                        .id(favourite.getMusic().getAlbum().getId())
                        .name(favourite.getMusic().getAlbum().getName())
                        .createdAt(favourite.getMusic().getAlbum().getCreatedAt())
                        .totalMusics(favourite.getMusic().getAlbum().getMusic().size())
                        .build()
                )
                .musician(MusicianOutputDTO.builder()
                    .id(favourite.getMusic().getMusician().getId())
                    .name(favourite.getMusic().getMusician().getName())
                    .email(favourite.getMusic().getMusician().getEmail())
                    .createdAt(favourite.getMusic().getMusician().getCreatedAt())
                    .role(favourite.getMusic().getMusician().getRole())
                    .build()
                )
                .build())
            .build();
    }
}
