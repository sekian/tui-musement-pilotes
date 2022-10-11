package com.tui.proof.repository;

import com.tui.proof.model.Client;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByFirstName(String firstName);

    List<Client> findByLastName(String lastName);

    List<Client> findByTelephone(String telephone);

    List<Client> findByEmail(String email);

    List<Client> findByFirstNameOrLastNameOrTelephoneOrEmail(String firstName, String lastName, String telephone, String email);

    List<Client> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrTelephoneIgnoreCaseContainingOrEmailIgnoreCaseContaining(String firstName, String lastName, String telephone, String email);
}