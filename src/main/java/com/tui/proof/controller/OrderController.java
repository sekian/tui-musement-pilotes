package com.tui.proof.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.service.OrderService;
import com.tui.proof.view.Views;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/api")
public class OrderController {

  @Autowired
  OrderService orderService;
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                  value = "{\n" +
                          "  \"client\": {\n" +
                          "    \"clientId\": 0\n" +
                          "  },\n" +
                          "  \"deliveryAddress\": {\n" +
                          "    \"street\": \"Campus Nord Jordi Girona\",\n" +
                          "    \"postcode\": \"08034\",\n" +
                          "    \"city\": \"Barcelona\",\n" +
                          "    \"country\": \"Spain\"\n" +
                          "  },\n" +
                          "  \"pilotes\": 15\n" +
                          "}",
                  summary = "User Authentication Example")))
  @JsonView(Views.Public.class)
  @RequestMapping(value="/order", method=RequestMethod.POST)
  ResponseEntity<Order> createOrderBody(@Valid @RequestBody(required=false) Order order) {
    return orderService.save(order);
  }

  @RequestMapping(value="/order/{id}", method=RequestMethod.PUT)
  ResponseEntity<?> modifyOrderBody(@PathVariable("id") String id, @Valid @RequestBody(required=false) Order order) {
    return orderService.put(id);
  }
  @JsonView(Views.Public.class)
  @RequestMapping(value="/search", method=RequestMethod.GET)
  @Parameter(name = "username", hidden = true)
  @Parameter(name = "password", hidden = true)
  ResponseEntity<?> searchByClient(@ParameterObject Client client) {
    log.info(client);
    return orderService.searchByClient(client);
  }
}
