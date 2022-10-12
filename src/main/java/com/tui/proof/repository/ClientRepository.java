package com.tui.proof.repository;

import com.tui.proof.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrTelephoneIgnoreCaseContainingOrEmailIgnoreCaseContaining(String firstName, String lastName, String telephone, String email);
}