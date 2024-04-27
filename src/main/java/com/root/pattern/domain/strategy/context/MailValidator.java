package com.root.pattern.domain.strategy.context;

import com.root.pattern.domain.strategy.EmailValidatorStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
public class MailValidator {
    private EmailValidatorStrategy emailValidatorStrategy;

    public boolean validate(String email) {
        return this.emailValidatorStrategy.validate(email);
    }
}
