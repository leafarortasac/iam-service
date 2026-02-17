package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.br.iam_service.IamServiceApplication;
import com.br.iam_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = IamServiceApplication.class)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("1. Deve registrar um usuário com sucesso")
    void deveRegistrarUsuario() throws Exception {
        String usuarioJson = """
        {
            "nome": "Rafael Castro",
            "email": "rafael@exemplo.com",
            "senha": "senha123",
            "role": "USER"
        }
        """;

        mockMvc.perform(post("/v1/usuario/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("2. Deve autenticar usuário e retornar Token JWT")
    void deveAutenticarELogar() throws Exception {

        deveRegistrarUsuario();

        var loginRequest = """
        {
            "email": "rafael@exemplo.com",
            "senha": "senha123"
        }
        """;

        mockMvc.perform(post("/v1/usuario/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("3. Deve retornar 409 ao tentar registrar e-mail duplicado")
    void deveRetornarConflito() throws Exception {

        deveRegistrarUsuario();

        String usuarioJson = """
        {
            "nome": "Rafael Castro",
            "email": "rafael@exemplo.com",
            "senha": "senha123",
            "role": "USER"
        }
        """;

        mockMvc.perform(post("/v1/usuario/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("E-mail já cadastrado no sistema"));
    }
}