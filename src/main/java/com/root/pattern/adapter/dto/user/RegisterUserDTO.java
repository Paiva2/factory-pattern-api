package com.root.pattern.adapter.dto.user;

import com.root.pattern.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserDTO {
    @NotEmpty
    @NotNull
    @NotBlank
    private String name;

    @NotEmpty
    @NotNull
    @NotBlank
    private String password;

    /*    @NotEmpty
        @NotNull
        @NotBlank
        @Email*/
    private String email;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .build();
    }
}
