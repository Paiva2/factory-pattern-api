package com.root.pattern.adapter.dto.musician;

import com.root.pattern.domain.entity.Musician;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginMusicianDTO {
    @NotEmpty
    @NotNull
    @NotBlank
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @NotEmpty
    @NotNull
    @NotBlank
    @Email
    private String email;

    public Musician toEntity() {
        return Musician.builder()
            .password(this.password)
            .email(this.email)
            .build();
    }
}
