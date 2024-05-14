package com.root.pattern.adapter.e2e.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.pattern.adapter.dto.user.RegisterUserDTO;
import com.root.pattern.domain.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class RegisterUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateANewUser() throws Exception {
        RegisterUserDTO payload = new RegisterUserDTO();
        payload.setName("any_name");
        payload.setPassword("123456");
        payload.setEmail("testinge2e@email.com");

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/user/register")
                .content(mapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.email").value(payload.getEmail()))
            .andExpect(jsonPath("$.name").value(payload.getName()))
            .andExpect(jsonPath("$.role").value(Role.USER.name()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    public void shouldThrowUnprocessableEntityIfPasswordIsLessThan6Chars() throws Exception {
        RegisterUserDTO payload = new RegisterUserDTO();
        payload.setName("any_name");
        payload.setPassword("12345");
        payload.setEmail("testinge2e@email.com");

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/user/register")
                .content(mapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(jsonPath("$.errors[0]").isNotEmpty())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldThrowUnprocessableEntityIfEmailIsNotValid() throws Exception {
        RegisterUserDTO payload = new RegisterUserDTO();
        payload.setName("any_name");
        payload.setPassword("123456");
        payload.setEmail("invalid_email");

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/user/register")
                .content(mapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(jsonPath("$.errors[0]").isNotEmpty())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldThrowUnprocessableEntityIfNameIsEmpty() throws Exception {
        RegisterUserDTO payload = new RegisterUserDTO();
        payload.setName("");
        payload.setPassword("123456");
        payload.setEmail("any_email@email.com");

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/user/register")
                .content(mapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(jsonPath("$.errors[0]").isNotEmpty())
            .andExpect(status().isUnprocessableEntity());
    }
}