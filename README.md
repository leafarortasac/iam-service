IAM Service (Identity & Access Management) 🔐

O IAM Service é o núcleo de segurança da arquitetura de microsserviços. Ele atua como um provedor de identidade (Identity Provider), sendo responsável por autenticar usuários e serviços integradores, emitindo tokens JWT (JSON Web Tokens) que garantem a integridade e a autoria das operações em todo o ecossistema.

🚀 Responsabilidades Principais

Gestão de Identidade: Registro e gerenciamento de usuários com persistência em banco de dados.

Autenticação M2M (Machine-to-Machine): Validação de credenciais de serviços integradores via Client ID e Client Secret.

Emissão de JWT: Geração de tokens assinados com chave secreta para tráfego seguro.

Segurança Centralizada: Definição de regras de acesso e expiração de sessões para qualquer serviço consumidor.

🛠️ Tecnologias e Ferramentas

Java 21 & Spring Boot 3

Spring Security: Framework base para a camada de proteção.

MongoDB: Banco de dados NoSQL para armazenamento de perfis de usuários e credenciais.

JJWT (Java JWT): Biblioteca para criação e parsing de tokens.

Swagger/OpenAPI: Documentação interativa e testes de endpoints.

Lombok: Produtividade no desenvolvimento.

📖 Documentação Interativa (Swagger)
Este serviço expõe uma interface Swagger UI para visualizar todos os endpoints (Login, Registro, Listagem de Usuários) e realizar testes de autenticação.

🔗 Acesse em: http://localhost:8080/swagger-ui/index.html (com o serviço rodando).

🗄️ Persistência de Dados
Diferente de implementações apenas em memória, este serviço utiliza o MongoDB para garantir a persistência escalável de:

Usuários: Nome, e-mail, senha (criptografada) e permissões.

Configurações de Acesso: Regras específicas por tipo de perfil.

🔐 Configurações de Integração

O IAM utiliza o conceito de Client Credentials para fluxos entre serviços e User Credentials para fluxos de usuários (Mobile/Web).

Credenciais de Integração (Ambiente de Teste)
Configuradas via application.yml:

Client ID: service-integration-provider

Client Secret: 7e5a8f42-c1b3-4d9a-8e2f-1a5c6b7d8e9f

Chave de Assinatura
A segurança é garantida por uma chave secreta (jwt-secret). Qualquer serviço que deseje validar o token emitido por este IAM deve possuir a mesma chave configurada.

📡 Fluxo de Autenticação
O cliente envia as credenciais (E-mail/Senha ou ClientID/Secret) via POST.

O IAM valida as informações contra o banco MongoDB.

É gerado um Bearer Token assinado.

O cliente utiliza este token no cabeçalho Authorization para consumir APIs protegidas.

Bash
# Exemplo de autenticação de usuário
  curl -X POST http://localhost:8080/v1/usuario/login \
  -H "Content-Type: application/json" \
  -d '{
  "email": "rafael.castromelo@gmail.com",
  "senha": "sua_senha_aqui"
  }'

📦 Como Instalar e Rodar

Certifique-se de que o MongoDB está rodando (via Docker ou local).

Instale as dependências compartilhadas:

Bash
mvn install
Na raiz deste projeto, execute:

Bash
mvn spring-boot:run
