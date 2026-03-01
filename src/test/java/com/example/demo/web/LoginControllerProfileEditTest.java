package com.example.demo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.domain.User;
import com.example.demo.service.AuthService;

@WebMvcTest(LoginController.class)
public class LoginControllerProfileEditTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void postProfileEdit_withoutSessionUser_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/profile/edit")
                .param("name", "New Name")
                .param("email", "saif0@my.yorku.ca"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void postProfileEdit_withSessionUser_callsServiceUpdatesSessionAndRedirectsToProfile() throws Exception {
        User sessionUser = new User("saif0@my.yorku.ca", "1234", "Saif");
        User updatedUser = new User("saif0@my.yorku.ca", "1234", "New Name");

        when(authService.updateProfile(any(User.class), eq("New Name"), eq("saif0@my.yorku.ca")))
                .thenReturn(updatedUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", sessionUser);

        mockMvc.perform(post("/profile/edit")
                .session(session)
                .param("name", "New Name")
                .param("email", "saif0@my.yorku.ca"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(request().sessionAttribute("user", updatedUser));
    }
}
