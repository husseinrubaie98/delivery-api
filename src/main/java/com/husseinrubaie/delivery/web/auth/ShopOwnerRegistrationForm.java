package com.husseinrubaie.delivery.web.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Form backing object for shop-owner registration (DP-532).
 *
 * <p>Carries the raw user input plus the declarative validation rules from the
 * story's acceptance criteria. Messages live in {@code messages.properties}.
 * Cross-field rules (password confirmation) are applied in the controller so the
 * error lands on the right field.</p>
 */
public class ShopOwnerRegistrationForm {

    @NotBlank(message = "{register.shopName.required}")
    @Size(min = 2, max = 100, message = "{register.shopName.size}")
    private String shopName;

    @NotBlank(message = "{register.email.required}")
    @Email(message = "{register.email.invalid}")
    @Size(max = 254, message = "{register.email.size}")
    private String email;

    /**
     * Strength rule per acceptance criteria: 8+ chars with upper, lower and digit.
     * The pattern accepts an empty value so a blank password reports only
     * "required", not every rule at once. Max 72 keeps within BCrypt's input limit.
     */
    @NotBlank(message = "{register.password.required}")
    @Size(min = 8, max = 72, message = "{register.password.size}")
    @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "{register.password.strength}")
    private String password;

    @NotBlank(message = "{register.confirmPassword.required}")
    private String confirmPassword;

    @NotBlank(message = "{register.phone.required}")
    @Pattern(regexp = "^$|^\\+?[0-9 ()-]{7,20}$", message = "{register.phone.invalid}")
    private String phone;

    @NotBlank(message = "{register.address.required}")
    @Size(min = 5, max = 255, message = "{register.address.size}")
    private String address;

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
