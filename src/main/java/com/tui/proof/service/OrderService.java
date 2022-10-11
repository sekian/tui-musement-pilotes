package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ClientRepository clientRepository;

    private static final long updateWindow = 300;

    public Order createOrder(Order order) {
        Optional<Client> client = clientRepository.findById(order.getClient().getClientId());
        client.ifPresent(order::setClient);
        order.setTimestamp(Instant.now());
        order.updateOrderTotal();
        Order _order = orderRepository.save(order);
        log.info(_order.toString());
        return _order;
    }

    public Order updateOrder(Order currOrder, Order newOrder) {
        currOrder.setPilotes(newOrder.getPilotes());
        currOrder.updateOrderTotal();
        currOrder.setDeliveryAddress(newOrder.getDeliveryAddress());
        Order _order = orderRepository.save(currOrder);
        log.info(_order.toString());
        return _order;
    }

    public Order getOrder(String id) {
        Optional<Order> _optionalOrder = orderRepository.findById(id);
        Order _order = null;
        if (_optionalOrder.isPresent()) {
            _order = _optionalOrder.get();
        }
        return _order;
    }

    public boolean isUpdatable(Order order) {
        Instant orderCreationDatePlusMinutes = order.getTimestamp().plusSeconds(updateWindow);
        return !Instant.now().isAfter(orderCreationDatePlusMinutes);
    }

    public List<Order> searchByClient(Client client) {
        List<Client> clients = clientRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrTelephoneIgnoreCaseContainingOrEmailIgnoreCaseContaining(
                client.getFirstName(), client.getLastName(), client.getTelephone(), client.getEmail());
//        List<Long> clientIds = clients.stream().map( Client::getClientId ).collect( Collectors.toList() );
        return orderRepository.findByClientIn(clients);
    }
}
