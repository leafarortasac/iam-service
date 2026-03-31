package controller;

import com.br.iam_service.controller.AuthController;
import com.br.iam_service.service.JwtService;
import com.br.shared.contracts.model.AuthResponseRepresentation;
import com.br.shared.contracts.model.ObterTokenRequestRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private final String CLIENT_ID = "my-client-id";
    private final String CLIENT_SECRET = "my-secret";

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(authController, "expectedClientId", CLIENT_ID);
        ReflectionTestUtils.setField(authController, "expectedClientSecret", CLIENT_SECRET);
    }

    @Test
    @DisplayName("Deve retornar token quando as credenciais do Client estiverem corretas")
    void deveObterTokenComSucesso() {

        var request = new ObterTokenRequestRepresentation();
        request.setClientId(CLIENT_ID);
        request.setClientSecret(CLIENT_SECRET);

        String tokenGerado = "jwt-token-test";
        when(jwtService.generateToken(CLIENT_ID)).thenReturn(tokenGerado);

        ResponseEntity<AuthResponseRepresentation> response = authController.obterToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tokenGerado, response.getBody().getAccessToken());
        assertEquals("Bearer", response.getBody().getTokenType());
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o Client Secret estiver incorreto")
    void deveRetornarUnauthorizedComSecretErrado() {

        var request = new ObterTokenRequestRepresentation();
        request.setClientId(CLIENT_ID);
        request.setClientSecret("senha-errada");

        ResponseEntity<AuthResponseRepresentation> response = authController.obterToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o Client ID não for encontrado")
    void deveRetornarUnauthorizedComClientIdErrado() {

        var request = new ObterTokenRequestRepresentation();
        request.setClientId("id-inexistente");
        request.setClientSecret(CLIENT_SECRET);

        ResponseEntity<AuthResponseRepresentation> response = authController.obterToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}