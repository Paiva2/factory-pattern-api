package com.root.pattern.adapter.dto.favourite;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ListOwnFavouritesDTO {
    int page;
    int perPage;
    long totalFavourites;
    List<FavouriteOutputDTO> favourites;
}
