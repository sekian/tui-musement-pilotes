package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {

    @Autowired
    ClientRepository clientRepository;

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    public UserService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    public UserDetails get(Authentication authentication) {
        return inMemoryUserDetailsManager.loadUserByUsername(authentication.getName());
    }

    public ResponseEntity<?> post(Client client) {
        try {
            Client _client = clientRepository.save(client);
            inMemoryUserDetailsManager.createUser(User.builder()
                    .passwordEncoder(password -> password)
                    .username(_client.getUsername())
                    .password(_client.getPassword())
                    .authorities("USER")
                    .roles("USER").build());
            log.info(_client.toString());
            return new ResponseEntity<>(_client, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error(e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
