package com.tui.proof.service;

import com.tui.proof.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthServiceTest {
    @Autowired
    AuthService authService;

    @Mock
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Test
    public void testAuthService() throws InterruptedException {
        Client client = createTestClient();

        String token1 = authService.getJWT(client);
        Thread.sleep(1000);
        String token2 = authService.getJWT(client);

        assertNotEquals(token1, token2);
        assertEquals(token1.length(), token2.length());
    }

    public Client createTestClient() {
        Client client = new Client();
        client.setEmail("example@example.com");
        client.setFirstName("FirstName");
        client.setLastName("LastName");
        client.setTelephone("123456789");
        client.setUsername("admin");
        client.setPassword("admin");
        client.setClientId(1);
        return client;
    }
}