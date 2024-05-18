package com.root.pattern.domain.usecase.favourite;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.favourite.FavouriteOutputDTO;
import com.root.pattern.adapter.dto.favourite.ListOwnFavouritesDTO;
import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Favourite;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
public class ListOwnFavouritesUsecase {
    private final UserDataProvider userDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    public ListOwnFavouritesDTO exec(Long userId, int page, int perPage) {
        this.validateInputs(userId);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Page<Favourite> userFavourites = this.getUserFavourites(user.getId(), page, perPage);

        return this.mountOutput(userFavourites);
    }

    private void validateInputs(Long userId) {
        if (Objects.isNull(userId)) {
            throw new BadRequestException("User id can't be empty");
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

    private Page<Favourite> getUserFavourites(Long userId, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.ASC, "favouriteOrder");

        return this.favouriteDataProvider.findAllByUser(userId, pageable);
    }

    private ListOwnFavouritesDTO mountOutput(Page<Favourite> favourites) {
        return ListOwnFavouritesDTO.builder()
            .page(favourites.getNumber() + 1)
            .perPage(favourites.getSize())
            .totalFavourites(favourites.getTotalElements())
            .favourites(favourites.getContent().stream().map(favourite ->
                    FavouriteOutputDTO.builder()
                        .id(favourite.getId())
                        .createdAt(favourite.getCreatedAt())
                        .favouriteOrder(favourite.getFavouriteOrder())
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
                            ).build()
                        ).build()
                ).collect(Collectors.toList())
            ).build();
    }
}
