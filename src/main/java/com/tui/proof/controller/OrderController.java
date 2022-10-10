package com.tui.proof.controller;

import com.tui.proof.model.Order;
import com.tui.proof.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
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

  @RequestMapping(value="/order", method=RequestMethod.POST)
  ResponseEntity<Order> createOrderBody(@Valid @RequestBody(required=false) Order order) {
    return orderService.save(order);

  }

  @RequestMapping(value="/order/{id}", method=RequestMethod.PUT)
  ResponseEntity<?> modifyOrderBody(@PathVariable("id") String id, @Valid @RequestBody(required=false) Order order) {
    return orderService.put(id);
  }

  @Operation(summary = "Get all orders", description = "Get all orders")
  @SecurityRequirement(name = "Bearer Authentication")
  @RequestMapping(value="/search", method=RequestMethod.GET)
  ResponseEntity<?> getOrderBody() {
    return orderService.search();
  }
}
