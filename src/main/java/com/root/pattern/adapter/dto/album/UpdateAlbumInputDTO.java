package com.root.pattern.adapter.dto.album;

import com.root.pattern.domain.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAlbumInputDTO {
    private String name;

    public Album toEntity(UUID id) {
        return Album.builder().id(id).name(this.name).build();
    }
}
