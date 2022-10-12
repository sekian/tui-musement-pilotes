package com.tui.proof.controller;

import com.tui.proof.model.Client;
import com.tui.proof.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/api/user")
@RestController
@Log4j2
public class UserController {
    @Autowired
    UserService userService;

    @Operation(summary = "Get user details", description = "The operation returns the details of the user that is associated with the provided JWT token.")
    @RequestMapping(value="/details", method=RequestMethod.GET)
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDetails getUserAuth(Authentication authentication) {
        return userService.getUserDetails(authentication);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n" +
                            "  \"firstName\": \"Name\",\n" +
                            "  \"lastName\": \"Surname\",\n" +
                            "  \"telephone\": \"123456789\",\n" +
                            "  \"email\": \"example@example.example\",\n" +
                            "  \"username\": \"neon\",\n" +
                            "  \"password\": \"1234\"\n" +
                            "}",
                    summary = "User Authentication Example")))
    @Operation(summary = "Create user", description = "Create user. The operation returns back the user details")
    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody(required=false) Client client) {
        try {
            Client _client = userService.createUser(client);
            return new ResponseEntity<>(_client, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error(e);
            ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
            String failedField = cause.getConstraintName();
            return new ResponseEntity<>(e.getCause().getMessage()+"\n"+failedField, HttpStatus.BAD_REQUEST);
        }
        catch (IllegalArgumentException e) {
            log.error(e);
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}