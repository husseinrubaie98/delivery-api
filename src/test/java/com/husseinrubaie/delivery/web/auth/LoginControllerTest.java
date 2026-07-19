package com.husseinrubaie.delivery.web.auth;

import com.husseinrubaie.delivery.security.SecurityConfig;
import com.husseinrubaie.delivery.security.WebTestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Web-slice tests for the login page (DP-566). Uses a test-scoped in-memory
 * user to exercise the real Spring Security form-login flow end to end:
 * email-as-username, failure redirect, and the 30-day remember-me cookie.
 */
@WebMvcTest(LoginController.class)
@Import({SecurityConfig.class, WebTestSecurityConfig.class})
class LoginControllerTest {

    private static final String EMAIL = WebTestSecurityConfig.TEST_EMAIL;
    private static final String PASSWORD = WebTestSecurityConfig.TEST_PASSWORD;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPageIsPublicAndRendersCustomView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void validCredentialsWithEmailParameterAuthenticate() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user(EMAIL).password(PASSWORD))
                .andExpect(authenticated().withUsername(EMAIL))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void invalidCredentialsRedirectBackWithError() throws Exception {
        mockMvc.perform(formLogin("/login").userParameter("email").user(EMAIL).password("wrong"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void rememberMeIssuesPersistentCookie() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                        .param("email", EMAIL)
                        .param("password", PASSWORD)
                        .param("remember-me", "on"))
                .andExpect(authenticated().withUsername(EMAIL))
                .andExpect(cookie().exists("remember-me"));
    }
}
