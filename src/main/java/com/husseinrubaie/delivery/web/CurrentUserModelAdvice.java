package com.husseinrubaie.delivery.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Exposes the signed-in user's email to every view as {@code currentUserEmail}
 * ({@code null} for anonymous visitors), so templates like the navbar can switch
 * between the signed-in and signed-out state without a hard dependency on
 * Spring Security's template extras.
 */
@ControllerAdvice
public class CurrentUserModelAdvice {

    @ModelAttribute("currentUserEmail")
    public String currentUserEmail(Principal principal) {
        return principal != null ? principal.getName() : null;
    }
}
