package com.tui.proof.controller;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    AuthenticationController authenticationController;

    @Test
    public void contextLoads() {
        Assertions.assertThat(authenticationController).isNotNull();
    }

}