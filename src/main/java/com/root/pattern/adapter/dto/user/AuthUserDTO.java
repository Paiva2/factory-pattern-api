package com.root.pattern.adapter.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDTO {
    String email;
    String authToken;
}
