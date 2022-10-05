package com.tui.proof.ws.controller;

import com.tui.proof.model.Address;
import com.tui.proof.model.Order;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RestController
@Validated
public class FooController {

  @Autowired
  ClientRepository clientRepository;
  @Autowired
  OrderRepository orderRepository;


  @GetMapping("/")
  void test() {
    log.info("Foo controller");
  }
  @Validated
  @RequestMapping(value="/createOrder", method=RequestMethod.POST)
  ResponseEntity<Order> createOrder(@RequestParam("pilotes") int pilotes,
                                    @RequestParam("country") String country,
                                    @RequestParam("city") String city,
                                    @RequestParam("street") String street,
                                    @RequestParam("postcode") String postcode) {
    Order order = new Order();
    order.setPilotes(pilotes);
    order.setOrderTotal(pilotes * 1.33);
    Address address = new Address();
    address.setCountry(country);
    address.setCity(city);
    address.setStreet(street);
    address.setPostcode(postcode);
    order.setDeliveryAddress(address);
    order.setTimestamp(Instant.now());
    try {
      Order _order = orderRepository.save(order);
      log.info(_order.toString());
      return new ResponseEntity<>(_order, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value="/createOrderBody", method=RequestMethod.POST)
  ResponseEntity<Order> createOrderBody(@Valid @RequestBody(required=false) Order order) {
    try {
      order.setTimestamp(Instant.now());
      order.updateOrderTotal();
      Order _order = orderRepository.save(order);
      log.info(_order.toString());
      return new ResponseEntity<>(_order, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value="/modifyOrderBody", method=RequestMethod.PUT)
  ResponseEntity<?> modifyOrderBody(@Valid @RequestBody(required=false) Order order) {
    try {
      Optional<Order> _OptionalOrder = orderRepository.findById(order.getNumber());
      Order _order = null;
      if (_OptionalOrder.isPresent()) {
        Order getOrder = _OptionalOrder.get();
        getOrder.updateOrderTotal();
        _order = orderRepository.save(getOrder);
      }
      else {
        return new ResponseEntity<>("id does not match any record in the database", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(_order, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
