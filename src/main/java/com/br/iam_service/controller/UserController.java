package com.br.iam_service.controller;

import com.br.iam_service.service.UserService;
import com.br.shared.contracts.api.UsuariosApi;
import com.br.shared.contracts.model.AuthResponseRepresentation;
import com.br.shared.contracts.model.LoginRequestRepresentation;
import com.br.shared.contracts.model.UsuarioRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsuariosApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UsuarioRepresentation> criaUsuario(
            final UsuarioRepresentation representation) {

        var usuarioCriado = userService.registrar(representation);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @Override
    public ResponseEntity<AuthResponseRepresentation> login(
            final LoginRequestRepresentation representation) {

        var response = userService.autenticar(representation);

        return ResponseEntity.ok(response);
    }
}
