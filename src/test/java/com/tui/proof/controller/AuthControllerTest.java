package com.tui.proof.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tui.proof.model.Client;
import com.tui.proof.service.AuthService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @MockBean
    AuthService authService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testLogin() throws Exception {
        JSONObject resp = new JSONObject();
        resp.put("clientId", 1);
        resp.put("token", "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWRtaW4iLCJleHAiOjE2NjU1MTM5NzYsImlhdCI6MTY2NTUxMzY3Niwic2NvcGUiOiJST0xFX1VTRVIifQ.Q756eXeAdtytQ2qbZb_18UkfDX4eaFqU7gKYd-h6Xlmgyc1bs0DhMzhFzOXtjrCZJ6f0PnTSQ1RdChceg6BKz04ATOVGVWTgyUzpBcExDi28qfongLoV7gl42PUX6kP9Foz5tGO_7fscL4Elohn1qv0P531CSiKyRX0EEx_6FuJSSojJV7LCXjZM8uf8DjCUKagydWuUjsY912s1j007OIII__3fxlo2MUaUf6MzMwUvU63vHwAmwyiJuZl4qApi0YveS_DkeVh0fyXuXtjMe44uXShGcTiv6kG0qRfFHTS21kvzxzYJJUD2j-p8TjP4XEXpX-l1iMBxmXGjtIpLoQ");
        Mockito.when(authService.getJWT(any(Client.class))).thenReturn(resp.toString());

        JSONObject body = new JSONObject();
        body.put("username", "admin");
        body.put("password", "admin");
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath(".clientId", Matchers.notNullValue()))
                .andExpect(jsonPath(".token", Matchers.notNullValue()))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        log.debug(content);
    }
}