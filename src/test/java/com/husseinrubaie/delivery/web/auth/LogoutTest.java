package com.husseinrubaie.delivery.web.auth;

import com.husseinrubaie.delivery.security.SecurityConfig;
import com.husseinrubaie.delivery.security.WebTestSecurityConfig;
import com.husseinrubaie.delivery.web.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-slice tests for logout (DP-583): the navbar affordance switches with
 * authentication state, and Spring Security's logout invalidates the session
 * and lands on the login page with a confirmation.
 */
@WebMvcTest({HomeController.class, LoginController.class})
@Import({SecurityConfig.class, WebTestSecurityConfig.class})
class LogoutTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void navbarOffersSignOutToAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/").with(user(WebTestSecurityConfig.TEST_EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign out")))
                .andExpect(content().string(containsString(WebTestSecurityConfig.TEST_EMAIL)))
                // The navbar's signed-out links disappear (the hero's own
                // "Get started" CTA is part of the page body, not the navbar).
                .andExpect(content().string(not(containsString("Sign in"))));
    }

    @Test
    void navbarOffersSignInToAnonymousVisitors() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign in")))
                .andExpect(content().string(not(containsString("Sign out"))));
    }

    @Test
    void logoutEndsSessionAndRedirectsToLogin() throws Exception {
        mockMvc.perform(logout())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void loginPageConfirmsSignOut() throws Exception {
        // Real query string (not param()): Spring Security's permit rules match the
        // full URL, so this must mirror the browser's GET /login?logout exactly.
        mockMvc.perform(get("/login?logout"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You have been signed out.")));
    }
}
