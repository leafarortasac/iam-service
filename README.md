IAM Service (Identity & Access Management) 🔐

O IAM Service é o núcleo de segurança da arquitetura de microsserviços. Ele atua como um provedor de identidade (Identity Provider), sendo responsável por autenticar requisições e emitir tokens JWT (JSON Web Tokens) que garantem a integridade e a autoria das operações em todo o ecossistema.

🚀 Responsabilidades Principais
Autenticação M2M (Machine-to-Machine): Validação de credenciais de serviços integradores.

Emissão de JWT: Geração de tokens assinados com chave secreta para tráfego seguro entre serviços.

Segurança: Centralização de regras de acesso e expiração de tokens.

🛠️ Tecnologias e Ferramentas
Java 21 & Spring Boot 3

Spring Security: Framework base para a camada de proteção.

JJWT (Java JWT): Biblioteca para criação e parsing de tokens.

Swagger/OpenAPI: Documentação interativa e testes de endpoints.

Lombok: Produtividade no desenvolvimento.

📖 Documentação Interativa (Swagger)
Para facilitar o teste e a integração, este serviço expõe uma interface Swagger UI. Com ela, você pode visualizar todos os endpoints e realizar chamadas de autenticação diretamente pelo navegador.

🔗 Acesse em: http://localhost:8080/swagger-ui.html (com o serviço rodando).

🔐 Configurações de Integração
Para fluxos de integração entre serviços, o IAM utiliza o conceito de Client ID e Client Secret.

Credenciais Padrão (Ambiente de Teste)
As seguintes credenciais estão configuradas via application.yml para demonstração:

Client ID: service-integration-provider

Client Secret: 7e5a8f42-c1b3-4d9a-8e2f-1a5c6b7d8e9f

Chave de Assinatura
A segurança dos tokens é garantida por uma chave secreta (jwt-secret). Certifique-se de que os serviços consumidores (pedido-service e order-service) utilizem a mesma chave para validar os tokens.

📡 Fluxo de Autenticação
O serviço/cliente envia as credenciais via POST para o endpoint de autenticação.

O IAM Service valida o clientId e o clientSecret.

É gerado um Bearer Token com tempo de expiração definido.

O cliente utiliza este token no cabeçalho Authorization para consumir as APIs protegidas.

Bash
# Exemplo de chamada via cURL
curl -X POST http://localhost:8083/auth/login \
-H "Content-Type: application/json" \
-d '{
"clientId": "service-integration-provider",
"clientSecret": "7e5a8f42-c1b3-4d9a-8e2f-1a5c6b7d8e9f"
}'
📦 Como Instalar e Rodar
Certifique-se de que a biblioteca Shared Contracts foi instalada localmente:

Bash
mvn install
Na raiz deste projeto, execute:

Bash
mvn spring-boot:run
