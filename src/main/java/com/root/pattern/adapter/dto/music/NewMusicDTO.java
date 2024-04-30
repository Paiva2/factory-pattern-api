package com.root.pattern.adapter.dto.music;

import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewMusicDTO {
    @Min(value = 60) // 60 secs
    private Long duration;

    @NotEmpty
    @NotNull
    @NotBlank
    private String name;

    private Boolean isSingle;

    private String albumId;

    public Music toEntity(UUID categoryId) {
        Music music = Music.builder()
            .duration(this.duration)
            .name(this.name)
            .album(Objects.isNull(this.albumId) ? null : Album.builder().id(UUID.fromString(this.albumId)).build())
            .category(Category.builder().id(categoryId).build())
            .isSingle(this.isSingle)
            .build();

        return music;
    }
}
