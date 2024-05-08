package com.root.pattern.adapter.dto.playlist;

import com.root.pattern.domain.entity.Playlist;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewPlaylistInputDTO {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    private String coverImage;

    public Playlist toEntity() {
        return Playlist.builder()
            .name(this.name)
            .coverImage(this.coverImage)
            .build();
    }
}
