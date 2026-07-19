package com.husseinrubaie.delivery.web.auth;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Password reset pages (DP-591).
 *
 * <p>Frontend slice covering the whole reset journey: request a link
 * ({@code /forgot-password}), the confirmation that a link was sent, the
 * set-new-password form the emailed token link lands on
 * ({@code /reset-password?token=...}, with an invalid/expired state), and the
 * final "password updated" confirmation. Token issuing, the 1-hour expiry and
 * the actual password change are delegated to the password-reset service when
 * the backend slice lands.</p>
 */
@Controller
public class PasswordResetController {

    static final String FORM_ATTR = "form";

    // --- Step 1: request a reset link -------------------------------------

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        if (!model.containsAttribute(FORM_ATTR)) {
            model.addAttribute(FORM_ATTR, new ForgotPasswordForm());
        }
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendResetLink(@Valid @ModelAttribute(FORM_ATTR) ForgotPasswordForm form,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/forgot-password";
        }

        // TODO(DP-591 backend): passwordResetService.requestReset(email) issues a
        //  unique 1-hour token and emails the link. The sent-page wording stays
        //  the same whether or not the account exists (no account enumeration).

        redirectAttributes.addFlashAttribute("email", form.getEmail());
        return "redirect:/forgot-password/sent";
    }

    @GetMapping("/forgot-password/sent")
    public String resetLinkSent() {
        return "auth/forgot-password-sent";
    }

    // --- Step 2: set a new password from the emailed link ------------------

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam(name = "token", required = false) String token,
                                    Model model) {
        // TODO(DP-591 backend): passwordResetService.validate(token) — real lookup
        //  and 1h expiry check; until then only token presence selects the view.
        if (!StringUtils.hasText(token)) {
            return "auth/reset-invalid";
        }
        if (!model.containsAttribute(FORM_ATTR)) {
            ResetPasswordForm form = new ResetPasswordForm();
            form.setToken(token);
            model.addAttribute(FORM_ATTR, form);
        }
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute(FORM_ATTR) ResetPasswordForm form,
                                BindingResult bindingResult) {
        if (!StringUtils.hasText(form.getToken())) {
            return "auth/reset-invalid";
        }
        if (!bindingResult.hasFieldErrors("confirmPassword") && !form.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword",
                    "reset.confirmPassword.mismatch", "Passwords do not match.");
        }
        if (bindingResult.hasErrors()) {
            return "auth/reset-password";
        }

        // TODO(DP-591 backend): passwordResetService.reset(token, newPassword) —
        //  consumes the token and replaces the credential (old password stops working).

        return "redirect:/reset-password/done";
    }

    @GetMapping("/reset-password/done")
    public String resetDone() {
        return "auth/reset-done";
    }
}
