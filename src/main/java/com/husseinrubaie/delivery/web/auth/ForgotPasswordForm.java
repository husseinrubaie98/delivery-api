package com.husseinrubaie.delivery.web.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Form backing object for requesting a password-reset link (DP-591).
 */
public class ForgotPasswordForm {

    @NotBlank(message = "{forgot.email.required}")
    @Email(message = "{forgot.email.invalid}")
    @Size(max = 254, message = "{forgot.email.size}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
