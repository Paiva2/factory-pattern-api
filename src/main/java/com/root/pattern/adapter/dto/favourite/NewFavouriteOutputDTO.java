package com.root.pattern.adapter.dto.favourite;

import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NewFavouriteOutputDTO {
    private UUID id;
    private Integer favouriteOrder;
    private Date createdAt;
    private MusicOutputDTO music;
}
