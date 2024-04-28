package com.root.pattern.adapter.dto.user;

import com.root.pattern.domain.entity.User;
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
public class UpdateUserProfileDTO {
    private String name;

    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @Email
    private String email;

    public User toEntity(Long id) {
        return User.builder()
            .id(id)
            .name(this.name)
            .password(this.password)
            .email(this.email)
            .build();
    }
}
