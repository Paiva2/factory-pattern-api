package com.root.pattern.domain.usecase.user;

import com.root.pattern.adapter.dto.user.ForgotPasswordOutputDTO;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.strategy.ForgotPasswordUsecaseStrategy;
import com.root.pattern.domain.strategy.concrete.ForgotMusicianPasswordStrategy;
import com.root.pattern.domain.strategy.concrete.ForgotUserPasswordStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class ForgotPasswordUsecase {
    @Value("${spring.mail.username}")
    private static String MAIL_USERNAME;
    private static String SUBJECT_FORGOT_PASSWORD = "New password - Playlist API";

    private final JavaMailSender mailSender;
    private final UserDataProvider userDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;

    private final static Map<String, ForgotPasswordUsecaseStrategy> STRATEGY = new HashMap<String, ForgotPasswordUsecaseStrategy>();

    @Transactional
    public ForgotPasswordOutputDTO exec(String userEmail, Role userType) {
        this.handleStrategy();

        return STRATEGY.get(userType.name()).exec(userEmail);
    }

    private void handleStrategy() {
        STRATEGY.put(Role.USER.name(), ForgotUserPasswordStrategy.builder()
            .passwordEncoder(this.passwordEncoder)
            .mailSender(this.mailSender)
            .userDataProvider(this.userDataProvider)
            .build()
        );

        STRATEGY.put(Role.USER.name(), ForgotMusicianPasswordStrategy.builder()
            .passwordEncoder(this.passwordEncoder)
            .mailSender(this.mailSender)
            .musicianDataProvider(this.musicianDataProvider)
            .build()
        );
    }
}
