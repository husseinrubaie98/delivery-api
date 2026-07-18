package com.husseinrubaie.delivery.web.auth;

import com.husseinrubaie.delivery.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Web-slice tests for the email verification landing page (DP-551): the link
 * must be publicly reachable (the user is not logged in yet) and render the
 * success or error state.
 */
@WebMvcTest(EmailVerificationController.class)
@Import(SecurityConfig.class)
class EmailVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void linkWithTokenIsPublicAndRendersSuccessState() throws Exception {
        mockMvc.perform(get("/verify-email").param("token", "some-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/verify-success"));
    }

    @Test
    void missingTokenRendersErrorState() throws Exception {
        mockMvc.perform(get("/verify-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/verify-error"));
    }

    @Test
    void blankTokenRendersErrorState() throws Exception {
        mockMvc.perform(get("/verify-email").param("token", "  "))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/verify-error"));
    }
}
