package com.tui.proof;

import com.tui.proof.controller.AuthController;
import com.tui.proof.controller.OrderController;
import com.tui.proof.controller.UserController;
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
class MainApplicationTest {

    @Autowired
    AuthController authController;

    @Autowired
    OrderController orderController;

    @Autowired
    UserController userController;

    @Test
    public void authenticationContextLoads() {
        Assertions.assertThat(authController).isNotNull();
    }

    @Test
    public void orderContextLoads() {
        Assertions.assertThat(orderController).isNotNull();
    }

    @Test
    public void userContextLoads() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    public void testMain() {
        MainApplication.main(new String[] {});
    }
}