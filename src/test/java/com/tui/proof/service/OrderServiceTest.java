package com.tui.proof.service;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ClientRepository clientRepository;

    @Test
    public void testOrderService() {
        Order order1 = createTestOrder();
        Order order2 = createTestOrder();
        Order order3 = createTestOrder();
        order1.setNumber("da549b22-e689-4855-afa9-9efca8ef8f2a");
        order2.setNumber("63eeb35d-3f99-4b05-a2b6-f3fb479fb996");
        order3.setNumber("b3d8231a-d84b-4fa0-a948-ec8d20a40c26");

        List<Order> orders = Arrays.asList(order1, order2, order3);
        List<Client> clients = Arrays.asList(order1.getClient(), order2.getClient());

        Mockito.when(orderRepository.findByClientIn(ArgumentMatchers.<Client>anyList())).thenReturn(orders);
        Mockito.when(orderRepository.findById("da549b22-e689-4855-afa9-9efca8ef8f2a")).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.findById("63eeb35d-3f99-4b05-a2b6-f3fb479fb996")).thenReturn(Optional.of(order2));
        Mockito.when(orderRepository.findById("b3d8231a-d84b-4fa0-a948-ec8d20a40c26")).thenReturn(Optional.of(order3));
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(order1);

        Mockito.when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(order1.getClient()));

        Order _orderCreate1 = orderService.createOrder(order1);
        assertEquals(_orderCreate1, order1);

        Order _orderRead1 = orderService.getOrder("da549b22-e689-4855-afa9-9efca8ef8f2a");
        Order _orderRead2 = orderService.getOrder("63eeb35d-3f99-4b05-a2b6-f3fb479fb996");
        Order _orderRead3 = orderService.getOrder("b3d8231a-d84b-4fa0-a948-ec8d20a40c26");

        assertEquals(_orderRead1.getNumber(), order1.getNumber());
        assertEquals(_orderRead2.getNumber(), order2.getNumber());
        assertEquals(_orderRead3.getNumber(), order3.getNumber());

        Order _orderUpdate1 = orderService.updateOrder(_orderRead1, _orderRead1);
        assertEquals(_orderUpdate1, order1);

        List<Order> _orders = orderService.searchByClient(order1.getClient());
        assertIterableEquals(_orders, orders);

        order3.setTimestamp(Instant.now());
        boolean isUpdatable = orderService.isUpdatable(order3);
        assertTrue(isUpdatable);
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
        Client client = createTestClient();
        order.setClient(client);
        return order;
    }
}