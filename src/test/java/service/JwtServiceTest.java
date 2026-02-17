package service;

import com.br.iam_service.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();
    private final String SECRET = "uma_chave_muito_secreta_com_mais_de_32_caracteres";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido com as claims corretas")
    void deveGerarTokenValido() {
        String email = "rafael@exemplo.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);

        var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(email, claims.getSubject());
        assertEquals("iam-service", claims.getIssuer());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}
