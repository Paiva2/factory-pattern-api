package com.root.pattern.application.factory.user;

import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.repository.UserDataProvider;
import com.root.pattern.domain.usecase.user.ForgotPasswordUsecase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class ForgotPasswordFactory {
    private final JavaMailSender mailSender;
    private final UserDataProvider userDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final PasswordEncoder passwordEncoder;

    @Bean("ForgotPasswordUsecase")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ForgotPasswordUsecase create() {
        return ForgotPasswordUsecase.builder()
            .mailSender(this.mailSender)
            .userDataProvider(this.userDataProvider)
            .passwordEncoder(this.passwordEncoder)
            .musicianDataProvider(musicianDataProvider)
            .build();
    }
}
