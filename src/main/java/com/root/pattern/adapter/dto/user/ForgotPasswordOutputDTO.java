package com.root.pattern.adapter.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPasswordOutputDTO {
    private String email;
}
