package com.br.iam_service.controller;

import com.br.iam_service.service.JwtService;
import com.br.shared.contracts.api.AutenticacaoApi;
import com.br.shared.contracts.model.AuthResponseRepresentation;
import com.br.shared.contracts.model.ObterTokenRequestRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AutenticacaoApi {

    private final JwtService jwtService;

    @Value("${iam.client-id}")
    private String expectedClientId;

    @Value("${iam.client-secret}")
    private String expectedClientSecret;


    @Override
    public ResponseEntity<AuthResponseRepresentation> obterToken(
            final ObterTokenRequestRepresentation representation) {

        String clientId = representation.getClientId();
        String clientSecret = representation.getClientSecret();

        if (expectedClientId.equals(clientId) && expectedClientSecret.equals(clientSecret)) {
            String token = jwtService.generateToken(clientId);

            var response = new AuthResponseRepresentation();
            response.setAccessToken(token);
            response.setTokenType("Bearer");
            response.setExpiresIn(3600);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}