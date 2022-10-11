package com.tui.proof.repository;


import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void testCRUD() {

        Assertions.assertThat(orderRepository).isNotNull();
        Assertions.assertThat(clientRepository).isNotNull();

        // Create
        Order order = createTestOrder();
        Order _order = orderRepository.save(order);

        // Read
        Iterable<Order> orders = orderRepository.findAll();
        Assertions.assertThat(orders).extracting(Order::getNumber).containsOnly(_order.getNumber());
        Assertions.assertThat(orders).extracting(Order::getPilotes).containsOnly(5);
        Assertions.assertThat(orders).extracting(Order::getTimestamp).containsOnly(order.getTimestamp());
        Assertions.assertThat(orders).extracting(Order::getDeliveryAddress).extracting(Address::getCountry).containsOnly(order.getDeliveryAddress().getCountry());

        // Update
        Optional<Order> optionalOrder = orderRepository.findById(_order.getNumber());
        Assertions.assertThat(optionalOrder.isPresent()).isTrue();
        Order putOrder = optionalOrder.get();
        putOrder.setPilotes(5);
        putOrder.updateOrderTotal();
        Order _putOrder = orderRepository.save(putOrder);
        Assertions.assertThat(putOrder.getNumber()).isEqualTo(_putOrder.getNumber());
        Assertions.assertThat(_putOrder.getPilotes()).isEqualTo(5);

        // Delete
        orderRepository.deleteAll();
        Assertions.assertThat(orderRepository.findAll()).isEmpty();
    }

    public Optional<Client> createTestClient() {
        Client client = new Client();
        Client _client = clientRepository.save(client);
        return clientRepository.findById(_client.getClientId());
    }

    public Order createTestOrder() {
        Order order = new Order();
        order.setPilotes(5);
        order.updateOrderTotal();
        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Barcelona");
        address.setStreet("Gran Via");
        address.setPostcode("08240");
        order.setDeliveryAddress(address);
        Instant instant = Instant.now();
        order.setTimestamp(instant);
        createTestClient().ifPresent(order::setClient);
        return order;
    }

}