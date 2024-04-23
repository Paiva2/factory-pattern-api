package com.root.pattern.adapter.dto.user;

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
    private Date createdAt;
}
