package com.tui.proof.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.service.OrderService;
import com.tui.proof.view.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
@Tag(name = "Order management", description = "Order API. Supports creating, updating and searching orders.")
public class OrderController {

  @Autowired
  OrderService orderService;
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                  value = "{\n" +
                          "  \"client\": {\n" +
                          "    \"clientId\": 1\n" +
                          "  },\n" +
                          "  \"deliveryAddress\": {\n" +
                          "    \"street\": \"Campus Nord Jordi Girona\",\n" +
                          "    \"postcode\": \"08034\",\n" +
                          "    \"city\": \"Barcelona\",\n" +
                          "    \"country\": \"Spain\"\n" +
                          "  },\n" +
                          "  \"pilotes\": 15\n" +
                          "}",
                  summary = "Create new order")))
  @JsonView(Views.Public.class)
  @Operation(summary = "Create order", description = "Creates a new order. The order must be associated to a client id that exists.")
  @RequestMapping(value="/order", method=RequestMethod.POST)
  public ResponseEntity<Order> createOrderBody(@Valid @RequestBody(required=false) Order order) {
    try {
      Order _order = orderService.createOrder(order);
      return new ResponseEntity<>(_order, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @io.swagger.v3.oas.annotations.media.Content(
                  mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                  value = "{\n" +
                          "  \"deliveryAddress\": {\n" +
                          "    \"street\": \"Campus Nord Jordi Girona\",\n" +
                          "    \"postcode\": \"08034\",\n" +
                          "    \"city\": \"Barcelona\",\n" +
                          "    \"country\": \"Spain\"\n" +
                          "  },\n" +
                          "  \"pilotes\": 15\n" +
                          "}",
                  summary = "Update existing order")))
  @JsonView(Views.Public.class)
  @RequestMapping(value="/order/{id}", method=RequestMethod.PUT)
  @Operation(summary = "Update order", description = "Updates the order associated to the given id. The order id must exist. The orders cannot be modified 5 minutes after creation.")
  public ResponseEntity<?> modifyOrderBody(@PathVariable("id") String id, @Valid @RequestBody(required=false) Order newOrder) {
    try {
      Order currOrder = orderService.getOrder(id);
      if (currOrder == null) {
        return new ResponseEntity<>("Provided id does not match any record in the database", HttpStatus.NOT_FOUND);
      }
      if (!orderService.isUpdatable(currOrder)) {
        return new ResponseEntity<>("Orders can only be updated during the 5 minutes after creation", HttpStatus.FORBIDDEN);
      }
      Order _order = orderService.updateOrder(currOrder, newOrder);
      log.info(_order.toString());
      return new ResponseEntity<>(_order, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @JsonView(Views.Public.class)
  @RequestMapping(value="/search", method=RequestMethod.GET)
  @Parameter(name = "username", hidden = true)
  @Parameter(name = "password", hidden = true)
  @Parameter(name = "orders", hidden = true)
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(summary = "Search orders", description = "Search orders by client fields. This operation performs a partial search of every field. This operation is secured by JWT bearer token.")
  public ResponseEntity<?> searchOrdersByClient(@ParameterObject Client client) {
    try {
      log.info(client);
      List<Order> orders = orderService.searchByClient(client);
      return new ResponseEntity<>(orders, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
