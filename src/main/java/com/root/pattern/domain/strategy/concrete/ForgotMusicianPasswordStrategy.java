package com.root.pattern.domain.strategy.concrete;

import com.root.pattern.adapter.dto.user.ForgotPasswordOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Email;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
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
public class ForgotMusicianPasswordStrategy implements ForgotPasswordUsecaseStrategy {
    @Value("${spring.mail.username}")
    private static String MAIL_USERNAME;
    private static String SUBJECT_FORGOT_PASSWORD = "New password - Playlist API";

    private final JavaMailSender mailSender;
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ForgotPasswordOutputDTO exec(String musicianEmail) {
        this.validateInputs(musicianEmail);

        Musician musician = this.checkIfMusicianExists(musicianEmail);
        this.checkIfMusicianIsDisabled(musician);

        String randomNewPassword = this.generateNewPassword();
        String newPasswordEncoded = this.encodeNewPassword(randomNewPassword);
        musician.setPassword(newPasswordEncoded);

        this.saveNewMusicianPassword(musician);

        Email newEmail = Email.builder()
            .from(MAIL_USERNAME)
            .to(musician.getEmail())
            .subject(SUBJECT_FORGOT_PASSWORD)
            .nameTo(musician.getName())
            .build();

        this.sendMailMessage(newEmail, randomNewPassword);

        return this.mountOutput(musician.getEmail());
    }

    private void validateInputs(String musicianEmail) {
        if (Objects.isNull(musicianEmail)) {
            throw new BadRequestException("Musician e-mail can't be empty");
        }
    }

    private Musician checkIfMusicianExists(String musicianEmail) {
        return this.musicianDataProvider.findByEmail(musicianEmail).orElseThrow(() -> new NotFoundException("Musician"));
    }

    private void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.getDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    private String generateNewPassword() {
        return UUID.randomUUID().toString();
    }

    private String encodeNewPassword(String newPassword) {
        return this.passwordEncoder.encode(newPassword);
    }

    private void saveNewMusicianPassword(Musician musician) {
        this.musicianDataProvider.update(musician);
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
