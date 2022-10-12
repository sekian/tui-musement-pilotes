package com.tui.proof;

import com.tui.proof.controller.AuthController;
import com.tui.proof.controller.OrderController;
import com.tui.proof.controller.UserController;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testRegisterUserAndPlaceOrderModifyOrderAndSearchOrder() throws JSONException {
        Client client = createTestClient();
        ResponseEntity<?> responseClient = userController.createUser(client);
        assertTrue(responseClient.getStatusCode().is2xxSuccessful());

        Client _client = (Client) responseClient.getBody();
        assertNotNull(_client);
        assertNotEquals(_client.getClientId(), 0);

        Order order = createTestOrder();
        order.setClient(_client);
        ResponseEntity<?> responseOrder = orderController.createOrderBody(order);
        assertTrue(responseOrder.getStatusCode().is2xxSuccessful());

        Order _order = (Order) responseOrder.getBody();
        assertNotNull(_order);
        assertNotNull(_order.getNumber());

        ResponseEntity<?> responseModifyOrder = orderController.modifyOrderBody(_order.getNumber(), order);
        assertTrue(responseOrder.getStatusCode().is2xxSuccessful());

        Order _orderModified = (Order) responseModifyOrder.getBody();
        assertNotNull(_orderModified);
        assertNotNull(_orderModified.getNumber());

        ResponseEntity<String> responseLogin = authController.login(_client);
        assertTrue(responseOrder.getStatusCode().is2xxSuccessful());
        String login = responseLogin.getBody();
        assertNotNull(login);

        JSONObject json = new JSONObject(login);
        long clientId = json.getInt("clientId");
        String token = json.getString("token");

        ResponseEntity<?> responseOrders = orderController.searchOrdersByClient(_client);
        assertTrue(responseOrder.getStatusCode().is2xxSuccessful());

        @SuppressWarnings("unchecked")
        List<Order> _orders = (List<Order>) responseOrders.getBody();
        assertNotNull(_orders);
        log.info(_orders);
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
        return order;
    }
}