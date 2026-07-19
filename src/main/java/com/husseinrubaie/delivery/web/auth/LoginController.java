package com.husseinrubaie.delivery.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Login page (DP-566).
 *
 * <p>Renders the custom login view only. Credential checking, session creation
 * and the error/success redirects are handled entirely by Spring Security's
 * form-login filter ({@code POST /login}) — see
 * {@link com.husseinrubaie.delivery.security.SecurityConfig}.</p>
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}
