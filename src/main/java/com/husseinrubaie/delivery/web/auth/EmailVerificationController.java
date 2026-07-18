package com.husseinrubaie.delivery.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Email verification landing page (DP-551).
 *
 * <p>Frontend slice: this is the page the verification link in the email points
 * to ({@code GET /verify-email?token=...}). It renders the verified state or the
 * invalid/expired state. Real token validation (lookup, 24h expiry, flipping
 * {@code email_verified}) is delegated to the verification service when the
 * backend slice of DP-551 lands; until then only token <em>presence</em> selects
 * the view so both states are reachable.</p>
 */
@Controller
public class EmailVerificationController {

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam(name = "token", required = false) String token) {
        // TODO(DP-551 backend): verificationService.verify(token) — validates the
        //  token, enforces the 24h expiry, and activates the account.
        if (!StringUtils.hasText(token)) {
            return "auth/verify-error";
        }
        return "auth/verify-success";
    }
}
