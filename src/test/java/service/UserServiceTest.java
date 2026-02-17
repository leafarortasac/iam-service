package service;

import com.br.iam_service.document.UserDocument;
import com.br.iam_service.exception.ConflictException;
import com.br.iam_service.mapper.UserMapper;
import com.br.iam_service.repository.UserRepository;
import com.br.iam_service.service.JwtService;
import com.br.iam_service.service.UserService;
import com.br.shared.contracts.model.LoginRequestRepresentation;
import com.br.shared.contracts.model.UsuarioRepresentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar token")
    void autenticarSucesso() {

        var request = new LoginRequestRepresentation("teste@email.com", "senha123");
        var userDoc = new UserDocument();
        userDoc.setEmail("teste@email.com");
        userDoc.setSenha("senha_criptografada");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userDoc));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("token_fake_jwt");

        var result = userService.autenticar(request);

        assertNotNull(result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
        verify(jwtService).generateToken("teste@email.com");
    }

    @Test
    @DisplayName("Deve lançar erro ao registrar e-mail duplicado")
    void registrarConflito() {

        var representation = new UsuarioRepresentation();
        representation.setEmail("duplicado@email.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.registrar(representation));
    }
}
