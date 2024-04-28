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
public class LoginUserDTO {
    @Email
    @NotBlank
    @NotNull
    @NotEmpty
    private String email;

    @NotBlank
    @NotNull
    @NotEmpty
    private String password;

    public User toEntity() {
        return User.builder().email(this.email).password(this.password).build();
    }
}
