package com.root.pattern.adapter.controller.user;

import com.root.pattern.adapter.dto.user.*;
import com.root.pattern.adapter.utils.JwtHandler;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.usecase.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {
    private final RegisterUserUsecase registerUserUseCase;
    private final AuthenticateUserUsecase authenticateUserUsecase;
    private final GetUserProfileUsecase getUserProfileUsecase;
    private final UpdateUserProfileUsecase updateUserProfileUsecase;
    private final ForgotPasswordUsecase forgotPasswordUsecase;

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
        @RequestBody @Valid LoginUserDTO dto
    ) {
        UserOutputDTO outputDTO = this.authenticateUserUsecase.exec(dto.toEntity());

        String authToken = this.jwtHandler.generate(
            String.valueOf(outputDTO.getId()),
            outputDTO.getRole()
        );

        return ResponseEntity.status(HttpStatus.OK).body(
            AuthUserDTO.builder().email(outputDTO.getEmail()).authToken(authToken).build()
        );
    }

    @Override
    public ResponseEntity<UserOutputDTO> getProfile(Authentication authentication) {
        UserOutputDTO outputDTO = this.getUserProfileUsecase.exec(Long.valueOf(authentication.getName()));

        return ResponseEntity.status(HttpStatus.OK).body(outputDTO);
    }

    @Override
    public ResponseEntity<UserOutputDTO> updateProfile(
        Authentication authentication,
        @RequestBody @Valid UpdateUserProfileDTO dto
    ) {
        Long userId = Long.valueOf(authentication.getName());
        UserOutputDTO output = this.updateUserProfileUsecase.exec(dto.toEntity(userId));

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<ForgotPasswordOutputDTO> forgotPassword(
        @RequestParam(value = "type") Role userType,
        @RequestBody @Valid ForgotPasswordDTO dto
    ) {
        ForgotPasswordOutputDTO output = this.forgotPasswordUsecase.exec(dto.getEmail(), userType);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
