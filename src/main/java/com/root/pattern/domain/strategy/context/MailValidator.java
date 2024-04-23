package com.root.pattern.domain.strategy.context;

import com.root.pattern.domain.strategy.EmailValidatorStrategy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailValidator {
    private final EmailValidatorStrategy emailValidatorStrategy;

    public boolean validate(String email) {
        return this.emailValidatorStrategy.validate(email);
    }
}
