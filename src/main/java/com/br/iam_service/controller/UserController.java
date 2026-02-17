package com.br.iam_service.controller;

import com.br.iam_service.document.UserDocument;
import com.br.iam_service.mapper.UserMapper;
import com.br.iam_service.service.UserService;
import com.br.shared.contracts.api.UsuariosApi;
import com.br.shared.contracts.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UsuariosApi {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<UsuarioRepresentation> criaUsuario(
            final UsuarioRepresentation representation) {

        var usuarioCriado = userService.registrar(representation);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @Override
    public ResponseEntity<UsuarioDocumentResponseRepresentation> getLogin(
            final String id,
            final String nome,
            final String email,
            final UserRoleRepresentation role,
            final Integer limit,
            final Boolean unPage) {

        Page<UserDocument> page = userService.getLogin(id,nome, email, role, limit, unPage);

        List<UserDocument> registros = page.getContent();

        var paginaInfo = new PaginaRepresentation();
        paginaInfo.setTotalPaginas(page.getTotalPages());
        paginaInfo.setTotalElementos(page.getTotalElements());

        var response = new UsuarioDocumentResponseRepresentation();
        response.setRegistros(userMapper.toListUsuarioDocumentoRepresentatio(registros));
        response.setPagina(paginaInfo);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponseRepresentation> login(
            final LoginRequestRepresentation representation) {

        var response = userService.autenticar(representation);

        return ResponseEntity.ok(response);
    }
}
