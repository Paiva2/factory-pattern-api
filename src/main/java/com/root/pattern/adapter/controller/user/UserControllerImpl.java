package com.root.pattern.adapter.controller.user;

import com.root.pattern.adapter.dto.user.RegisterUserDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.usecase.user.RegisterUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {
    private final RegisterUserUseCase registerUserUseCase;

    @Override
    public ResponseEntity<UserOutputDTO> registerNewUser(
            @RequestBody @Valid RegisterUserDTO dto
    ) {
        User newUser = this.registerUserUseCase.exec(dto.toEntity());

        return ResponseEntity.status(200).body(UserOutputDTO.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .name(newUser.getName())
                .createdAt(newUser.getCreatedAt())
                .build()
        );
    }
}
