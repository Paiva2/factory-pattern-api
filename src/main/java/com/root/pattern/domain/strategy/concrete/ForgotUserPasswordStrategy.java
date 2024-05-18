package com.root.pattern.domain.strategy.concrete;

import com.root.pattern.adapter.dto.user.ForgotPasswordOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Email;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.strategy.ForgotPasswordUsecaseStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class ForgotUserPasswordStrategy implements ForgotPasswordUsecaseStrategy {
    @Value("${spring.mail.username}")
    private static String MAIL_USERNAME;
    private static String SUBJECT_FORGOT_PASSWORD = "New password - Playlist API";

    private final JavaMailSender mailSender;
    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ForgotPasswordOutputDTO exec(String userEmail) {
        this.validateInputs(userEmail);

        User user = this.checkIfUserExists(userEmail);
        this.checkIfUserIsDisabled(user);

        String randomNewPassword = this.generateNewPassword();
        String newPasswordEncoded = this.encodeNewPassword(randomNewPassword);
        user.setPassword(newPasswordEncoded);

        this.saveNewUserPassword(user);

        Email newEmail = Email.builder()
            .from(MAIL_USERNAME)
            .to(user.getEmail())
            .subject(SUBJECT_FORGOT_PASSWORD)
            .nameTo(user.getName())
            .build();

        this.sendMailMessage(newEmail, randomNewPassword);

        return this.mountOutput(user.getEmail());
    }

    private void validateInputs(String userEmail) {
        if (Objects.isNull(userEmail)) {
            throw new BadRequestException("User e-mail can't be empty");
        }
    }

    private User checkIfUserExists(String userEmail) {
        return this.userDataProvider.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("User"));
    }

    private void checkIfUserIsDisabled(User user) {
        if (user.isDisabled()) {
            throw new ForbiddenException("User is disabled");
        }
    }

    private String generateNewPassword() {
        return UUID.randomUUID().toString();
    }

    private String encodeNewPassword(String newPassword) {
        return this.passwordEncoder.encode(newPassword);
    }

    private void saveNewUserPassword(User user) {
        this.userDataProvider.update(user);
    }

    private void sendMailMessage(Email email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getFrom());
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.forgotPasswordMessage(newPassword));

        this.mailSender.send(message);
    }

    private ForgotPasswordOutputDTO mountOutput(String email) {
        return ForgotPasswordOutputDTO.builder()
            .email(email)
            .build();
    }
}
