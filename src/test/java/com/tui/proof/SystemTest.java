package com.tui.proof;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SystemTest {

    @LocalServerPort
    private int port;

    @Test
    public void testCreateUserAndCreateOrderAndUpdateOrderAndSearchOrder() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        String baseURL = "http://localhost:" + port;
        String userURL = baseURL + "/api/user";
        Client client = createTestClient();
        ResponseEntity<Client> entityClient = restTemplate.postForEntity(userURL, client, Client.class);
        Client _client = entityClient.getBody();
        Assertions.assertThat(_client).isNotNull();
        Assertions.assertThat(_client.getClientId()).isPositive();

        String orderURL = baseURL + "/api/order";
        Order order = createTestOrder();
        order.setClient(_client);
        ResponseEntity<Order> entityOrder = restTemplate.postForEntity(orderURL, order, Order.class);
        Order _order = entityOrder.getBody();
        Assertions.assertThat(_order).isNotNull();
        Assertions.assertThat(_order.getNumber()).isNotNull();
        Assertions.assertThat(_order.getTimestamp()).isNotNull();

        String orderPutURL = orderURL + "/" + _order.getNumber();
        HttpEntity<Order> httpEntity = new HttpEntity<>(_order);
        ResponseEntity<Order> entityPutOrder = restTemplate.exchange(orderPutURL, HttpMethod.PUT, httpEntity, Order.class);
        Order _putOrder = entityPutOrder.getBody();
        Assertions.assertThat(_putOrder).isNotNull();
        Assertions.assertThat(_putOrder.getNumber()).isNotNull();
        Assertions.assertThat(_putOrder.getNumber()).isEqualTo(_order.getNumber());

        String loginURL = baseURL + "/api/auth/login";
        ResponseEntity<String> entityLogin = restTemplate.postForEntity(loginURL, _client, String.class);
        String login = entityLogin.getBody();
        assertNotNull(login);

        JSONObject json = new JSONObject(login);
        long clientId = json.getInt("clientId");
        String token = json.getString("token");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);

        String searchURL = baseURL + "/api/search";
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(searchURL)
                .queryParam("firstName", "{firstName}")
                .queryParam("lastName", "{lastName}")
                .queryParam("telephone", "{telephone}")
                .queryParam("email", "{email}")
                .encode()
                .toUriString();

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("firstName", "");
        queryParams.put("lastName", "");
        queryParams.put("telephone", "");
        queryParams.put("email", "example");

        HttpEntity<String> httpEntitySearch = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<Order>> entitySearch = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                httpEntitySearch,
                new ParameterizedTypeReference<List<Order>>() {},
                queryParams);
        List<Order> _orders = entitySearch.getBody();
        Assertions.assertThat(_orders).isNotNull();
        Assertions.assertThat(_orders).isNotEmpty();
        log.info(_orders);
    }

    @Test
    public void testReadClientFromJWT() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();

        String baseURL = "http://localhost:" + port;
        String userURL = baseURL + "/api/user";
        Client client = createTestClient();
        client.setUsername("neo");
        ResponseEntity<Client> entityClient = restTemplate.postForEntity(userURL, client, Client.class);
        Client _client = entityClient.getBody();
        Assertions.assertThat(_client).isNotNull();
        Assertions.assertThat(_client.getClientId()).isPositive();

        String loginURL = baseURL + "/api/auth/login";
        ResponseEntity<String> entityLogin = restTemplate.postForEntity(loginURL, _client, String.class);
        String login = entityLogin.getBody();
        assertNotNull(login);

        JSONObject json = new JSONObject(login);
        long clientId = json.getInt("clientId");
        String token = json.getString("token");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);

        String userDetailsURL = baseURL + "/api/user/details";
        HttpEntity<String> httpEntitySearch = new HttpEntity<>(httpHeaders);
        ResponseEntity<Client> entityUserDetails = restTemplate.exchange(
                userDetailsURL,
                HttpMethod.GET,
                httpEntitySearch,
                Client.class
        );
        Client _clientDetails = entityUserDetails.getBody();
        assertNotNull(_clientDetails);
        log.info(_clientDetails);
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
        return order;
    }
}
