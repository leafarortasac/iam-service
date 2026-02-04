package com.br.iam_service.controller;

import com.br.iam_service.dto.AuthRequest;
import com.br.iam_service.dto.AuthResponse;
import com.br.iam_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class IamController {

    private final JwtService jwtService;

    @Value("${iam.client-id}")
    private String expectedClientId;

    @Value("${iam.client-secret}")
    private String expectedClientSecret;

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody @Valid AuthRequest request) {

        if (expectedClientId.equals(request.getClientId()) &&
                expectedClientSecret.equals(request.getClientSecret())) {

            String token = jwtService.generateToken(request.getClientId());

            AuthResponse response = AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(3600)
                    .build();

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}