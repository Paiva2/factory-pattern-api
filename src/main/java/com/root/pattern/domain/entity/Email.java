package com.root.pattern.domain.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Email {
    private String from;
    private String to;
    private String subject;
    private String message;
    private String nameTo;

    public String forgotPasswordMessage(String newPassword) {
        StringBuilder builder = new StringBuilder();

        builder.append("Hello ").append(this.nameTo).append("! ").append("\n")
            .append("You requested a new password for Playlist API, here's your new password: ").append(newPassword).append("\n")
            .append("Don't forget to store your new password on a safe place or to change it later ;)");

        return builder.toString();
    }
}
