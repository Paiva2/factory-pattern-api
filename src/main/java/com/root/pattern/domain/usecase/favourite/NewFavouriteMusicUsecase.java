package com.root.pattern.domain.usecase.favourite;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.favourite.NewFavouriteOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Favourite;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.interfaces.repository.FavouriteDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class NewFavouriteMusicUsecase {
    private final UserDataProvider userDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final FavouriteDataProvider favouriteDataProvider;

    public NewFavouriteOutputDTO exec(Long userId, UUID musicId) {
        this.validateInputs(userId, musicId);

        User user = this.checkIfUserExists(userId);
        this.checkIfUserIsDisabled(user);

        Music music = this.checkIfMusicExists(musicId);
        this.checkIfMusicIsDisabled(music);

        this.checkIfUserAlreadyFavouritedMusic(user.getId(), music.getId());

        Favourite newFavourite = this.insertFavourite(this.mountNewFavourite(user, music));

        return this.mountOutput(newFavourite);
    }

    private void validateInputs(Long userId, UUID musicId) {
        if (Objects.isNull(musicId)) {
            throw new BadRequestException("Favourite music id can't be empty");
        }

        if (Objects.isNull(userId)) {
            throw new BadRequestException("Favourite user id can't be empty");
        }
    }

    private Favourite mountNewFavourite(User user, Music music) {
        return Favourite.builder()
            .user(user)
            .music(music)
            .build();
    }

    private User checkIfUserExists(Long userId) {
        return this.userDataProvider.findById(userId).orElseThrow(() -> new NotFoundException("User"));
    }

    private void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    private Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    private void checkIfMusicIsDisabled(Music music) {
        if (music.getDisabled()) {
            throw new ForbiddenException("Music is disabled");
        }
    }

    private void checkIfUserAlreadyFavouritedMusic(Long userId, UUID musicId) {
        Optional<Favourite> favourite = this.favouriteDataProvider.findByUserAndMusic(userId, musicId);

        if (favourite.isPresent()) {
            throw new ConflictException("User already added this music to favourite");
        }
    }

    private Integer getLastOrderOnUserFavourites(Long userId) {
        return this.favouriteDataProvider.getLastOrderOnUserFavourites(userId);
    }

    private Favourite insertFavourite(Favourite newFavourite) {
        Integer currentLastOrderOnFav = this.getLastOrderOnUserFavourites(newFavourite.getUser().getId());
        Integer newOrder = Objects.isNull(currentLastOrderOnFav) || currentLastOrderOnFav < 1 ? 0 : currentLastOrderOnFav + 1;

        newFavourite.setFavouriteOrder(newOrder);

        return this.favouriteDataProvider.create(newFavourite);
    }

    private NewFavouriteOutputDTO mountOutput(Favourite favourite) {
        return NewFavouriteOutputDTO.builder()
            .id(favourite.getId())
            .favouriteOrder(favourite.getFavouriteOrder())
            .createdAt(favourite.getCreatedAt())
            .music(MusicOutputDTO.builder()
                .id(favourite.getMusic().getId())
                .duration(favourite.getMusic().getDuration())
                .name(favourite.getMusic().getName())
                .isSingle(favourite.getMusic().getIsSingle())
                .order(Objects.nonNull(favourite.getMusic().getAlbumOrder()) ? Math.toIntExact(favourite.getMusic().getAlbumOrder()) : null)
                .disabled(favourite.getMusic().getDisabled())
                .createdAt(favourite.getMusic().getCreatedAt())
                .category(CategoryOutputDTO.builder()
                    .id(favourite.getMusic().getCategory().getId())
                    .name(favourite.getMusic().getCategory().getName().name())
                    .build()
                )
                .build()
            )
            .build();
    }
}
