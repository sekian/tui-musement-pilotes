package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Test
    public void testUserService() {
        Client client = createTestClient();
        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(client);
        Client _client = userService.createUser(client);
        assertEquals(_client, client);
    }

    public Client createTestClient() {
        Client client = new Client();
        client.setEmail("example@example.com");
        client.setFirstName("FirstName");
        client.setLastName("LastName");
        client.setTelephone("123456789");
        client.setUsername("test");
        client.setPassword("test");
        client.setClientId(1);
        return client;
    }
}