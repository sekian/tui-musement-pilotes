package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderService {

//    @Autowired
    private final OrderRepository orderRepository;

//    @Autowired
    private final ClientRepository clientRepository;

    private static long updateWindow = 300;

    @Autowired
    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<Order> save(Order order) {
        try {
            Optional<Client> client = clientRepository.findById(order.getClient().getClientId());
            client.ifPresent(order::setClient);
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
                Instant orderCreationDatePlusMinutes = getOrder.getTimestamp().plusSeconds(updateWindow);
                if (Instant.now().isAfter(orderCreationDatePlusMinutes)) {
                    return new ResponseEntity<>("Cannot modify order data after 5 minutes", HttpStatus.BAD_REQUEST);
                }
                getOrder.updateOrderTotal();
                _order = orderRepository.save(getOrder);
            }
            else {
                return new ResponseEntity<>("Provided id does not match any record in the database", HttpStatus.NOT_FOUND);
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

    public ResponseEntity<?> searchByClient(Client client) {
        try {
            List<Client> clients = clientRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrTelephoneIgnoreCaseContainingOrEmailIgnoreCaseContaining(
                    client.getFirstName(), client.getLastName(), client.getTelephone(), client.getEmail());
            List<Long> clientIds = clients.stream().map( Client::getClientId ).collect( Collectors.toList() );
            List<Order> orders = orderRepository.findByClientIn(clients);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
