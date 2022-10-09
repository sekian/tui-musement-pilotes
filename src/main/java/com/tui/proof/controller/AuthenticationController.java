package com.tui.proof.controller;

//import com.tui.proof.model.User;
import com.tui.proof.model.Client;
import com.tui.proof.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations to register and login only.")
public class AuthenticationController {
    @Autowired
    AuthService authService;

    @Operation(summary = "User Authentication", description = "Authenticate the user and return a JWT token if the user is valid.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = "{\n" + "  \"username\": \"jane\",\n" + "  \"password\": \"password\"\n" + "}",
                            summary = "User Authentication Example")))
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody Client user) {
        return authService.get(user);
    }
}