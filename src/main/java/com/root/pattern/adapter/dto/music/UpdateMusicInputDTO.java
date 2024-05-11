package com.root.pattern.adapter.dto.music;

import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import lombok.*;

import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateMusicInputDTO {
    @Min(value = 60) // 60 secs
    private Long duration;

    private String name;

    private Boolean isSingle;

    private UUID categoryId;

    private Long albumOrder;

    public Music toEntity(UUID musicId) {
        return Music.builder()
            .id(musicId)
            .name(this.name)
            .isSingle(this.isSingle)
            .duration(this.duration)
            .albumOrder(this.albumOrder)
            .category(Category.builder()
                .id(Objects.nonNull(this.categoryId) ? this.categoryId : null)
                .build()
            )
            .build();
    }
}
