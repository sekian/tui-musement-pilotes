package com.tui.proof.controller;

import com.tui.proof.model.Client;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;

@RequestMapping("/api/user")
@RestController
@Log4j2
//@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "Get user details", description = "Get the user details. The operation returns the details of the user that is associated " + "with the provided JWT token.")
    @RequestMapping(value="/details", method=RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDetails getUserAuth(Authentication authentication) {
        return userService.get(authentication);
    }

    @Operation(summary = "Create user", description = "Create user. The operation returns back the user details")
    @RequestMapping(value="", method=RequestMethod.POST)
    @Parameter(hidden = true)
    public ResponseEntity<?> createUser(@Valid @RequestBody(required=false) Client client) {
        return userService.post(client);
    }
}