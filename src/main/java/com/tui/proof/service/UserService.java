package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.repository.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public UserDetails getUserDetails(Authentication authentication) {
        return inMemoryUserDetailsManager.loadUserByUsername(authentication.getName());
    }

    public Client createUser(Client client) {
        Client _client = clientRepository.save(client);
        inMemoryUserDetailsManager.createUser(User.builder()
                .passwordEncoder(password -> password)
                .username(_client.getUsername())
                .password(_client.getPassword())
                .authorities("USER")
                .roles("USER").build());
        log.info(_client.toString());
        return _client;
    }
}
