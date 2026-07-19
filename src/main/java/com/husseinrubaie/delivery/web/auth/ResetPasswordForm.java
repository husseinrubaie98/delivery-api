package com.husseinrubaie.delivery.web.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Form backing object for setting a new password from a reset link (DP-591).
 *
 * <p>The opaque reset token travels with the form as a hidden field; it is
 * checked for presence here and validated for real (lookup + 1h expiry) by the
 * password-reset service when the backend slice lands. The password rules match
 * registration ({@link ShopOwnerRegistrationForm}).</p>
 */
public class ResetPasswordForm {

    private String token;

    @NotBlank(message = "{reset.password.required}")
    @Size(min = 8, max = 72, message = "{reset.password.size}")
    @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "{reset.password.strength}")
    private String password;

    @NotBlank(message = "{reset.confirmPassword.required}")
    private String confirmPassword;

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
