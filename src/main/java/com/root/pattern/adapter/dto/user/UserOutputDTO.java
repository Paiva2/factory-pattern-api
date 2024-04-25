package com.root.pattern.adapter.dto.user;

import com.root.pattern.domain.enums.Role;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOutputDTO {
    private Long id;
    private String email;
    private String name;
    private Role role;
    private Date createdAt;
}
