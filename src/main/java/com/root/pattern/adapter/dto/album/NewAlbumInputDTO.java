package com.root.pattern.adapter.dto.album;

import com.root.pattern.domain.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewAlbumInputDTO {
    @NotBlank
    @NotEmpty
    @NotNull
    String albumName;

    public Album toEntity() {
        return Album.builder().name(this.albumName).build();
    }
}
