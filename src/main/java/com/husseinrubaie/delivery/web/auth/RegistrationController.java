package com.husseinrubaie.delivery.web.auth;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Shop-owner registration pages (DP-532).
 *
 * <p>Frontend slice: renders the registration form, applies the acceptance-criteria
 * validation, and on success follows POST-redirect-GET to the "check your email"
 * page. The actual account creation + verification email are delegated to the
 * registration service when the backend slice of DP-532 lands.</p>
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    static final String FORM_ATTR = "form";

    @GetMapping
    public String registrationForm(Model model) {
        if (!model.containsAttribute(FORM_ATTR)) {
            model.addAttribute(FORM_ATTR, new ShopOwnerRegistrationForm());
        }
        return "auth/register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute(FORM_ATTR) ShopOwnerRegistrationForm form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("confirmPassword") && !form.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword",
                    "register.confirmPassword.mismatch", "Passwords do not match.");
        }
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // TODO(DP-532 backend): registrationService.registerShopOwner(form -> DTO)
        //  creates the inactive account and sends the verification email.

        redirectAttributes.addFlashAttribute("email", form.getEmail());
        return "redirect:/register/check-email";
    }

    /** Post-registration landing page: tells the user a verification email is on its way. */
    @GetMapping("/check-email")
    public String checkEmail() {
        return "auth/check-email";
    }
}
