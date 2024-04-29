package com.root.pattern.adapter.dto.musician;

import com.root.pattern.domain.entity.Musician;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateMusicianProfileDTO {
    private String name;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @Email
    private String email;

    public Musician toEntity(Long id) {
        return Musician.builder()
            .id(id)
            .name(this.name)
            .password(this.password)
            .email(this.email)
            .build();
    }
}
