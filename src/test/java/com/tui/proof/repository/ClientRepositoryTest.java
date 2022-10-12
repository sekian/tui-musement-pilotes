package com.tui.proof.repository;

import com.tui.proof.model.Client;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@Log4j2
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {
    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testCRUD() {

        Assertions.assertThat(clientRepository).isNotNull();

        // Create
        Client client = createTestClient();
        Client _client = clientRepository.save(client);

        // Read
        Iterable<Client> clients = clientRepository.findAll();
        Assertions.assertThat(clients).extracting(Client::getClientId).containsOnly(_client.getClientId());
        Assertions.assertThat(clients).extracting(Client::getEmail).containsOnly(client.getEmail());
        Assertions.assertThat(clients).extracting(Client::getUsername).containsOnly(client.getUsername());

        // Update
        Optional<Client> optionalClient = clientRepository.findById(_client.getClientId());
        Assertions.assertThat(optionalClient.isPresent()).isTrue();
        Client putClient = optionalClient.get();
        putClient.setLastName("NewLastName");
        putClient.setTelephone("987654321");
        Client _putClient = clientRepository.save(putClient);
        Assertions.assertThat(putClient.getClientId()).isEqualTo(_putClient.getClientId());
        Assertions.assertThat(_putClient.getLastName()).isEqualTo("NewLastName");
        Assertions.assertThat(_putClient.getTelephone()).isEqualTo("987654321");

        // Delete
        clientRepository.deleteAll();
        Assertions.assertThat(clientRepository.findAll()).isEmpty();
    }
    public Client createTestClient() {
        Client client = new Client();
        client.setEmail("example@example.com");
        client.setFirstName("FirstName");
        client.setLastName("LastName");
        client.setTelephone("123456789");
        client.setUsername("test");
        client.setPassword("test");
        return client;
    }
}