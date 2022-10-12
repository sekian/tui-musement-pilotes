package com.tui.proof.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tui.proof.model.Client;
import com.tui.proof.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateUser() throws Exception {
        Client client = createTestClient();
        client.setClientId(1);
        Mockito.when(userService.createUser(any(Client.class))).thenReturn(client);

        JSONObject body = new JSONObject();
        body.put("firstName", "FirstName");
        body.put("lastName", "LastName");
        body.put("telephone", "123456789");
        body.put("email", "example@example.com");
        body.put("username", "neo");
        body.put("password", "1234");
        MvcResult result = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(body.toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(jsonPath(".username", Matchers.notNullValue()))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        log.debug(content);
    }

    public Client createTestClient() {
        Client client = new Client();
        client.setEmail("example@example.com");
        client.setFirstName("FirstName");
        client.setLastName("LastName");
        client.setTelephone("123456789");
        client.setUsername("neo");
        client.setPassword("1234");
        return client;
    }

}