package com.tui.proof.repository;

import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
//    List<Order> findByClientId(Long clientId);
    List<Order> findByClientIn(List<Client> clients);
}