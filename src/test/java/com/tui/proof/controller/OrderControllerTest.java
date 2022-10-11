package com.tui.proof.controller;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderControllerTest {
    @Autowired
    AuthenticationController authenticationController;

    @Test
    public void contextLoads() {
        Assertions.assertThat(authenticationController).isNotNull();
    }
}