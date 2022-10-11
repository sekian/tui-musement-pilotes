package com.tui.proof.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.tui.proof.model.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final JwtEncoder encoder;

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public AuthService(InMemoryUserDetailsManager inMemoryUserDetailsManager, JwtEncoder encoder) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.encoder = encoder;
    }

    public String getJWT(Client user) {
        UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(user.getUsername());
        if (user.getPassword().equalsIgnoreCase(userDetails.getPassword())) {
            String token = generateToken(userDetails);
            JSONObject body = new JSONObject();
            body.put("token", token);
            body.put("clientId", user.getClientId());
            return body.toString();
        }
        return null;
    }

    private String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        long expiry = 300L;
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
