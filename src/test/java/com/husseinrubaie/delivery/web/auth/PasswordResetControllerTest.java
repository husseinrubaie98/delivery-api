package com.husseinrubaie.delivery.web.auth;

import com.husseinrubaie.delivery.security.SecurityConfig;
import com.husseinrubaie.delivery.security.WebTestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Web-slice tests for the password reset journey (DP-591): requesting a link,
 * the sent confirmation, the token-guarded set-new-password form with the
 * acceptance-criteria password rules, and the final confirmation. Also covers
 * the "Forgot password?" entry point on the login page.
 */
@WebMvcTest({PasswordResetController.class, LoginController.class})
@Import({SecurityConfig.class, WebTestSecurityConfig.class})
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static MockHttpServletRequestBuilder resetSubmission(String password, String confirmPassword) {
        return post("/reset-password").with(csrf())
                .param("token", "some-token")
                .param("password", password)
                .param("confirmPassword", confirmPassword);
    }

    // --- Entry point -------------------------------------------------------

    @Test
    void loginPageLinksToForgotPassword() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/forgot-password")));
    }

    // --- Request a reset link ----------------------------------------------

    @Test
    void forgotPasswordPageIsPublicAndRendersForm() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"))
                .andExpect(model().attributeExists(PasswordResetController.FORM_ATTR));
    }

    @Test
    void validEmailRedirectsToSentConfirmation() throws Exception {
        mockMvc.perform(post("/forgot-password").with(csrf())
                        .param("email", "owner@shop.example"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgot-password/sent"))
                .andExpect(flash().attribute("email", "owner@shop.example"));
    }

    @Test
    void invalidEmailRerendersWithFieldError() throws Exception {
        mockMvc.perform(post("/forgot-password").with(csrf())
                        .param("email", "not-an-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"))
                .andExpect(model().attributeHasFieldErrors(
                        PasswordResetController.FORM_ATTR, "email"));
    }

    @Test
    void sentConfirmationPageIsPublic() throws Exception {
        mockMvc.perform(get("/forgot-password/sent"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password-sent"));
    }

    // --- Set a new password ------------------------------------------------

    @Test
    void resetFormRendersWhenTokenPresent() throws Exception {
        mockMvc.perform(get("/reset-password").param("token", "some-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attributeExists(PasswordResetController.FORM_ATTR));
    }

    @Test
    void missingTokenShowsInvalidLinkPage() throws Exception {
        mockMvc.perform(get("/reset-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-invalid"));
    }

    @Test
    void validResetRedirectsToDoneConfirmation() throws Exception {
        mockMvc.perform(resetSubmission("NewStr0ngPass", "NewStr0ngPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reset-password/done"));
    }

    @Test
    void weakPasswordRerendersResetFormWithFieldError() throws Exception {
        mockMvc.perform(resetSubmission("alllowercase1", "alllowercase1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attributeHasFieldErrors(
                        PasswordResetController.FORM_ATTR, "password"));
    }

    @Test
    void mismatchedConfirmationFlagsConfirmPasswordField() throws Exception {
        mockMvc.perform(resetSubmission("NewStr0ngPass", "Different1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attributeHasFieldErrors(
                        PasswordResetController.FORM_ATTR, "confirmPassword"));
    }

    @Test
    void resetSubmissionWithoutTokenShowsInvalidLinkPage() throws Exception {
        mockMvc.perform(post("/reset-password").with(csrf())
                        .param("password", "NewStr0ngPass")
                        .param("confirmPassword", "NewStr0ngPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-invalid"));
    }

    @Test
    void doneConfirmationPageIsPublic() throws Exception {
        mockMvc.perform(get("/reset-password/done"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-done"));
    }
}
