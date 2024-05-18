package com.root.pattern.domain.strategy;

import com.root.pattern.adapter.dto.user.ForgotPasswordOutputDTO;

public interface ForgotPasswordUsecaseStrategy {
    ForgotPasswordOutputDTO exec(String email);
}
