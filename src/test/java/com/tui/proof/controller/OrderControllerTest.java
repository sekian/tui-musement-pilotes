package com.tui.proof.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.service.AuthService;
import com.tui.proof.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
    @MockBean
    OrderService orderService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateOrderBody() throws Exception {
        Order order = createTestOrder();
        Mockito.when(orderService.createOrder(any(Order.class))).thenReturn(order);

        JSONObject body = new JSONObject();
        JSONObject client = new JSONObject();
        client.put("clientId", 1);
        JSONObject deliveryAddress = new JSONObject();
        deliveryAddress.put("street", "Gran Via");
        deliveryAddress.put("postcode", "08034");
        deliveryAddress.put("city", "Barcelona");
        deliveryAddress.put("country", "Spain");
        body.put("client", client);
        body.put("deliveryAddress", deliveryAddress);
        body.put("pilotes", 15);

        MvcResult result = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath(".timestamp", Matchers.notNullValue()))
                .andExpect(jsonPath(".pilotes", Matchers.equalTo(Collections.singletonList(15))))
                .andExpect(jsonPath("$.deliveryAddress.postcode", Matchers.equalTo("08034")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        log.debug(content);
    }

    @Test
    public void testModifyOrderBody() throws Exception {
        Order currOrder = createTestOrder();
        Order newOrder = createTestOrder();
        newOrder.setPilotes(5);
        newOrder.updateOrderTotal();
        currOrder.setNumber("96ff1285-3450-467f-aa86-785308cf30b7");
        newOrder.setNumber("96ff1285-3450-467f-aa86-785308cf30b7");
        Mockito.when(orderService.getOrder("96ff1285-3450-467f-aa86-785308cf30b7")).thenReturn(currOrder);
        Mockito.when(orderService.isUpdatable(any(Order.class))).thenReturn(true);
        Mockito.when(orderService.updateOrder(any(Order.class), any(Order.class))).thenReturn(newOrder);

        JSONObject body = new JSONObject();
        JSONObject client = new JSONObject();
        client.put("clientId", 1);
        JSONObject deliveryAddress = new JSONObject();
        deliveryAddress.put("street", "Gran Via");
        deliveryAddress.put("postcode", "08034");
        deliveryAddress.put("city", "Barcelona");
        deliveryAddress.put("country", "Spain");
        body.put("client", client);
        body.put("deliveryAddress", deliveryAddress);
        body.put("pilotes", 5);

        MvcResult result = mockMvc.perform(put("/api/order/96ff1285-3450-467f-aa86-785308cf30b7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.toString())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath(".timestamp", Matchers.notNullValue()))
                .andExpect(jsonPath(".pilotes", Matchers.equalTo(Collections.singletonList(5))))
                .andExpect(jsonPath("$.deliveryAddress.postcode", Matchers.equalTo("08034")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        log.debug(content);
    }

    @Test
    public void testSearchOrdersByClient() throws Exception {
        Order order1 = createTestOrder();
        Order order2 = createTestOrder();
        order1.setNumber("96ff1285-3450-467f-aa86-785308cf30b7");
        order2.setNumber("1e91d2f9-35dc-4d86-a57e-6e8e610de79d");
        order2.setPilotes(5);
        order2.updateOrderTotal();
        Client client1 = createTestClient();
        order1.setClient(client1);
        order2.setClient(client1);
        List<Order> orders = Arrays.asList(order1, order2);
        Mockito.when(orderService.searchByClient(any(Client.class))).thenReturn(orders);

        MvcResult result = mockMvc.perform(get("/api/search")
                        .param("clientId", "1")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("telephone", "")
                        .param("email", "example")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath(".number", Matchers.notNullValue()))
                .andExpect(jsonPath(".pilotes", Matchers.notNullValue()))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        log.debug(content);
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
        order.setPilotes(15);
        order.updateOrderTotal();
        Address address = new Address();
        address.setCountry("Spain");
        address.setCity("Barcelona");
        address.setStreet("Gran Via");
        address.setPostcode("08034");
        order.setDeliveryAddress(address);
        Instant instant = Instant.now();
        order.setTimestamp(instant);
        return order;
    }
}