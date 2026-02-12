package com.br.iam_service.service;

import com.br.iam_service.exception.ConflictException;
import com.br.iam_service.exception.UnauthorizedException;
import com.br.iam_service.mapper.UserMapper;
import com.br.iam_service.repository.UserRepository;
import com.br.shared.contracts.model.AuthResponseRepresentation;
import com.br.shared.contracts.model.LoginRequestRepresentation;
import com.br.shared.contracts.model.UsuarioRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseRepresentation autenticar(
            final LoginRequestRepresentation request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user.getEmail());

        var response = new AuthResponseRepresentation();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        response.setUsuario(userMapper.toRepresentation(user));

        return response;
    }

    public UsuarioRepresentation registrar(
            final UsuarioRepresentation representation) {

        if (userRepository.existsByEmail(representation.getEmail())) {
            throw new ConflictException("E-mail já cadastrado no sistema");
        }

        var userDocument = userMapper.toDocument(representation);

        userDocument.setId(UUID.randomUUID().toString());
        userDocument.setSenha(passwordEncoder.encode(representation.getSenha()));

        var savedUser = userRepository.save(userDocument);

        return userMapper.toRepresentation(savedUser);
    }
}
