package com.tui.proof.service;

import com.tui.proof.model.Order;
import com.tui.proof.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public ResponseEntity<Order> save(Order order) {
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

    public ResponseEntity<?> put(String id) {
        try {
            Optional<Order> _OptionalOrder = orderRepository.findById(id); //order.getNumber()
            Order _order = null;
            if (_OptionalOrder.isPresent()) {
                Order getOrder = _OptionalOrder.get();
                getOrder.updateOrderTotal();
                _order = orderRepository.save(getOrder);
            }
            else {
                return new ResponseEntity<>("id does not match any record in the database", HttpStatus.NOT_FOUND);
            }
            log.info(_order.toString());
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> search() {
        try {
            List<Order> orders = new ArrayList<>(orderRepository.findAll());
            if (orders.isEmpty()) {
                return new ResponseEntity<>("NO_CONTENT", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
