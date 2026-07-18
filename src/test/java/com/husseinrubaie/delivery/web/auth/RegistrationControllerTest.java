package com.husseinrubaie.delivery.web.auth;

import com.husseinrubaie.delivery.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Web-slice tests for shop-owner registration (DP-532): public access, the
 * acceptance-criteria validation rules, and the POST-redirect-GET success flow.
 */
@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * A submission that is valid except for the given password pair.
     * (Duplicate {@code param()} calls concatenate values, so tests must not
     * "override" a param — they pass the variant explicitly.)
     */
    private static MockHttpServletRequestBuilder submission(String password, String confirmPassword) {
        return post("/register").with(csrf())
                .param("shopName", "Rubaie Electronics")
                .param("email", "owner@shop.example")
                .param("password", password)
                .param("confirmPassword", confirmPassword)
                .param("phone", "+964 770 123 4567")
                .param("address", "Baghdad, Al-Mansour, Street 14");
    }

    private static MockHttpServletRequestBuilder validSubmission() {
        return submission("Str0ngPass", "Str0ngPass");
    }

    @Test
    void registrationPageIsPublicAndRendersForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists(RegistrationController.FORM_ATTR));
    }

    @Test
    void validSubmissionRedirectsToCheckEmail() throws Exception {
        mockMvc.perform(validSubmission())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register/check-email"))
                .andExpect(flash().attribute("email", "owner@shop.example"));
    }

    @Test
    void weakPasswordRerendersFormWithFieldError() throws Exception {
        mockMvc.perform(submission("alllowercase1", "alllowercase1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors(
                        RegistrationController.FORM_ATTR, "password"));
    }

    @Test
    void mismatchedConfirmationFlagsConfirmPasswordField() throws Exception {
        mockMvc.perform(submission("Str0ngPass", "Different1"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors(
                        RegistrationController.FORM_ATTR, "confirmPassword"));
    }

    @Test
    void blankSubmissionReportsAllRequiredFields() throws Exception {
        mockMvc.perform(post("/register").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors(
                        RegistrationController.FORM_ATTR,
                        "shopName", "email", "password", "confirmPassword", "phone", "address"));
    }

    @Test
    void checkEmailPageIsPublic() throws Exception {
        mockMvc.perform(get("/register/check-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/check-email"));
    }
}
