package com.root.pattern.adapter.controller.user;

import com.root.pattern.adapter.dto.user.AuthUserDTO;
import com.root.pattern.adapter.dto.user.AuthenticateUserDTO;
import com.root.pattern.adapter.dto.user.RegisterUserDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import com.root.pattern.adapter.utils.JwtHandler;
import com.root.pattern.domain.usecase.user.AuthenticateUserUsecase;
import com.root.pattern.domain.usecase.user.RegisterUserUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {
    private final RegisterUserUsecase registerUserUseCase;
    private final AuthenticateUserUsecase authenticateUserUsecase;
    private final JwtHandler jwtHandler;

    @Override
    public ResponseEntity<UserOutputDTO> registerNewUser(
            @RequestBody @Valid RegisterUserDTO dto
    ) {
        UserOutputDTO newUser = this.registerUserUseCase.exec(dto.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Override
    public ResponseEntity<AuthUserDTO> authenticateUser(
            @RequestBody @Valid AuthenticateUserDTO dto
    ) {
        UserOutputDTO outputDTO = this.authenticateUserUsecase.exec(dto.toEntity());

        String authToken = this.jwtHandler.generate(String.valueOf(outputDTO.getId()));

        return ResponseEntity.status(HttpStatus.OK).body(
                AuthUserDTO.builder().email(outputDTO.getEmail()).authToken(authToken).build()
        );
    }
}
