# 🎰 Aposta Consciente XP - API REST

Projeto acadêmico desenvolvido com foco em **Arquitetura Orientada a Serviços (SOA)**, implementando boas práticas de engenharia de software, autenticação segura com JWT, análise de risco comportamental e gestão de apostas responsáveis.

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Autenticação JWT](#autenticação-jwt)
- [Endpoints da API](#endpoints-da-api)
- [Exemplos de Uso](#exemplos-de-uso)
- [Testes](#testes)
- [Análise de Risco](#análise-de-risco)
- [Conformidade SOLID](#conformidade-solid)
- [Contribuidores](#contribuidores)

---

## 🎯 Visão Geral

O **Aposta Consciente XP** é uma API REST que gerencia apostas esportivas, de cassino e loteria com foco em responsabilidade financeira. O sistema monitora padrões de comportamento e alerta usuários sobre riscos potenciais, sugerindo alternativas de investimento mais seguras.

**Objetivos principais:**
- Implementar autenticação e autorização robustas
- Gerenciar apostas com limites automáticos de proteção
- Analisar comportamentos de risco em tempo real
- Fornecer documentação automática da API
- Manter separação clara de responsabilidades (SOLID)

---

## 🚀 Tecnologias

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 17+ | Linguagem principal |
| Spring Boot | 3.2.0 | Framework web |
| Spring Security | 3.2.0 | Autenticação/Autorização |
| Spring Data JPA | 3.2.0 | Persistência de dados |
| MySQL | 8.0+ | Banco de dados relacional |
| JWT (jjwt) | 0.11.5 | Tokens JWT |
| Flyway | 9.x | Migrações de banco |
| SpringDoc OpenAPI | 2.5.0 | Documentação automática |
| JUnit 5 | 5.9+ | Testes unitários |
| Mockito | 5.x | Mocks para testes |
| Maven | 3.6+ | Gerenciador de dependências |

---

## 📋 Requisitos

### Pré-requisitos do Sistema
- **Java Development Kit (JDK) 17+** instalado
- **MySQL Server 8.0+** em execução
- **Maven 3.6+** para build e execução
- **Git** para clonar o repositório
- Um cliente HTTP (Postman, Insomnia, ou curl)

### Verificar Versões
```bash
java -version      # Verifica JDK
mysql --version    # Verifica MySQL
mvn --version      # Verifica Maven
```

---

## ⚙️ Configuração

### 1. Clonar o Repositório
```bash
git clone https://github.com/LucasVinicius45/Consciousbet.git
cd consciousbet
```

### 2. Configurar Banco de Dados MySQL
```sql
CREATE DATABASE consciousbet;
CREATE USER 'consciousbet'@'localhost' IDENTIFIED BY 'sua_senha_secreta';
GRANT ALL PRIVILEGES ON consciousbet.* TO 'consciousbet'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar Arquivo application.properties
Edite `src/main/resources/application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/consciousbet?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=consciousbet
spring.datasource.password=sua_senha_secreta

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# Server
server.port=8080

# Logging
logging.level.br.com.fiap.consciousbet=DEBUG
```

---

## ▶️ Execução

### Opção 1: Maven (Recomendado)
```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### Opção 2: JAR Executável
```bash
# Build do JAR
mvn clean package

# Executar o JAR
java -jar target/consciousbet-1.0.0.jar
```

### Opção 3: IDE (IntelliJ IDEA / Eclipse)
1. Abra o projeto na IDE
2. Configure o SDK Java 17+
3. Execute a classe `ConsciousBetApplication.java`

### Verificar se a Aplicação está Rodando
```bash
curl http://localhost:8080/swagger-ui.html
```

Se conseguir acessar o Swagger UI, a aplicação está funcionando!

---

## 📂 Estrutura do Projeto

```
src/main/java/br/com/fiap/consciousbet/
├── config/                    # Configurações gerais
│   ├── SecurityConfig.java   # Configuração Spring Security
│   └── SwaggerConfig.java    # Configuração OpenAPI/Swagger
│
├── controller/               # Camada de Apresentação
│   ├── AuthController.java
│   ├── BetController.java
│   ├── UserController.java
│   └── RiskAnalysisController.java
│
├── dto/                      # Data Transfer Objects
│   ├── UserCreateDTO, UserUpdateDTO, UserResponseDTO
│   ├── BetCreateDTO, BetUpdateDTO, BetResponseDTO
│   └── AlertResponse
│
├── entity/                   # Entidades JPA
│   ├── User.java
│   ├── Bet.java
│   └── UserAuth.java
│
├── exception/               # Tratamento de Exceções
│   └── GlobalExceptionHandler.java
│
├── mapper/                  # Conversão Entity ↔ DTO
│   ├── UserMapper.java
│   └── BetMapper.java
│
├── repository/              # Acesso a Dados
│   ├── UserRepository.java
│   ├── BetRepository.java
│   └── UserAuthRepository.java
│
├── security/                # Autenticação/Autorização
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
│
├── service/                 # Interfaces de Serviço
│   ├── UserService.java
│   ├── BetService.java
│   └── RiskAnalysisService.java
│
└── serviceimpl/             # Implementações de Serviço
    ├── UserServiceImpl.java
    ├── BetServiceImpl.java
    └── RiskAnalysisServiceImpl.java

src/main/resources/
├── db/migration/            # Scripts Flyway
│   ├── V1__Create_all_tables.sql
│   ├── V2__Create_auth_users.sql
│   ├── V3__Add_timestamps_to_users.sql
│   └── V4__Update_bets_table.sql
└── application.properties   # Configurações da aplicação

src/test/java/br/com/fiap/consciousbet/
├── service/
│   ├── BetServiceTest.java
│   └── UserServiceTest.java
└── controller/
    ├── BetControllerIntegrationTest.java
    └── UserControllerIntegrationTest.java
```

---

## 🔐 Autenticação JWT

### Fluxo de Autenticação

```
1. Cliente faz POST em /auth/login com email e senha
   ↓
2. Servidor valida credenciais no banco
   ↓
3. Servidor gera JWT assinado com chave secreta
   ↓
4. Cliente recebe token e incluir em Authorization header
   ↓
5. JwtAuthenticationFilter valida token em cada requisição
   ↓
6. Se válido → Requisição processada
   Se inválido/expirado → 401 Unauthorized
```

### 1. Fazer Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@email.com",
  "password": "123456"
}
```

**Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJpYXQiOjE2OTk4MDAwMDAsImV4cCI6MTY5OTgxODAwMH0.signature",
  "email": "admin@email.com",
  "message": "Login successful",
  "tokenType": "Bearer"
}
```

### 2. Usar Token em Requisições Protegidas
```http
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 3. Validar Token
```http
GET /auth/validate
Authorization: Bearer seu_token_aqui
```

### 4. Renovar Token
```http
POST /auth/refresh
Authorization: Bearer seu_token_antigo
```

---

## 📡 Endpoints da API

### 🔑 Autenticação (`/auth`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/auth/login` | Faz login e obtém JWT | ❌ |
| GET | `/auth/validate` | Valida um token JWT | ✅ |
| POST | `/auth/refresh` | Renova um token | ✅ |

### 👤 Usuários (`/api/users`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/users` | Cria novo usuário | ✅ |
| GET | `/api/users` | Lista usuários (paginado) | ✅ |
| GET | `/api/users/list` | Lista todos (sem paginação) | ✅ |
| GET | `/api/users/{id}` | Obtém usuário por ID | ✅ |
| GET | `/api/users/email/{email}` | Obtém usuário por email | ✅ |
| PUT | `/api/users/{id}` | Atualiza usuário completo | ✅ |
| PATCH | `/api/users/{id}` | Atualiza parcialmente | ✅ |
| DELETE | `/api/users/{id}` | Deleta usuário | ✅ |
| GET | `/api/users/count` | Conta total de usuários | ✅ |
| GET | `/api/users/exists/email/{email}` | Verifica se email existe | ✅ |

### 🎲 Apostas (`/api/bets`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/bets` | Cria nova aposta | ✅ |
| GET | `/api/bets` | Lista apostas (paginado) | ✅ |
| GET | `/api/bets/list` | Lista todas (sem paginação) | ✅ |
| GET | `/api/bets/{id}` | Obtém aposta por ID | ✅ |
| GET | `/api/bets/user/{userId}` | Apostas de um usuário | ✅ |
| GET | `/api/bets/user/{userId}/paginated` | Apostas com paginação | ✅ |
| GET | `/api/bets/user/{userId}/recent` | Apostas últimas 24h | ✅ |
| GET | `/api/bets/type/{type}` | Apostas por tipo | ✅ |
| GET | `/api/bets/status/{status}` | Apostas por status | ✅ |
| PUT | `/api/bets/{id}` | Atualiza aposta completa | ✅ |
| PATCH | `/api/bets/{id}` | Atualiza parcialmente | ✅ |
| PATCH | `/api/bets/{id}/status` | Atualiza apenas status | ✅ |
| PATCH | `/api/bets/{id}/cancel` | Cancela aposta | ✅ |
| DELETE | `/api/bets/{id}` | Deleta aposta | ✅ |
| GET | `/api/bets/user/{userId}/stats` | Estatísticas do usuário | ✅ |
| GET | `/api/bets/user/{userId}/can-bet` | Verifica se pode apostar | ✅ |

### 🚨 Análise de Risco (`/api/bets/alerts`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/bets/alerts/{userId}` | Analisa risco comportamental | ✅ |

---

## 📋 Exemplos de Uso

### Criar Usuário
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer seu_token" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "age": 28
  }'
```

### Criar Aposta
```bash
curl -X POST http://localhost:8080/api/bets \
  -H "Authorization: Bearer seu_token" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 150.00,
    "type": "SPORTS",
    "description": "Brasil vs Argentina - Copa do Mundo"
  }'
```

### Buscar Análise de Risco
```bash
curl -X GET http://localhost:8080/api/bets/alerts/1 \
  -H "Authorization: Bearer seu_token"
```

### Atualizar Status de Aposta
```bash
curl -X PATCH http://localhost:8080/api/bets/1/status?status=WON \
  -H "Authorization: Bearer seu_token"
```

### Listar Apostas de um Usuário
```bash
curl -X GET http://localhost:8080/api/bets/user/1 \
  -H "Authorization: Bearer seu_token"
```

### Obter Estatísticas de Apostas
```bash
curl -X GET http://localhost:8080/api/bets/user/1/stats \
  -H "Authorization: Bearer seu_token"
```

---

## 🧪 Testes

### Rodar Todos os Testes
```bash
mvn test
```

### Rodar Testes de um Módulo Específico
```bash
mvn test -Dtest=BetServiceTest
mvn test -Dtest=BetControllerIntegrationTest
```

### Rodar com Cobertura de Código
```bash
mvn test jacoco:report
# Relatório em: target/site/jacoco/index.html
```

### Estrutura de Testes

#### Testes Unitários
- **BetServiceTest**: Testa lógica de negócio de apostas
    - Criação de aposta com dados válidos
    - Validação de limites diários
    - Cálculo de totais e contagens

- **UserServiceTest**: Testa operações com usuários
    - CRUD completo de usuários
    - Validação de email único
    - Busca por email

#### Testes de Integração
- **BetControllerIntegrationTest**: Testa endpoints de apostas
    - Criação com autenticação
    - Validação de dados de entrada
    - Listagem e paginação
    - Atualização de status
    - Deleção de apostas

- **UserControllerIntegrationTest**: Testa endpoints de usuários
    - CRUD com autenticação
    - Listagem com filtros
    - Tratamento de erros

### Exemplo: Executar Teste Específico
```bash
mvn test -Dtest=BetServiceTest#testCreateBetSuccess
```

---

## 🔍 Análise de Risco

### Critérios de Risco

O sistema monitora apostas e gera alertas com base em:

| Critério | Limite | Ação |
|----------|--------|------|
| Número de apostas | ≥ 5 em 24h | ⚠️ Alerta Alto |
| Valor total | > R$ 1.000 em 24h | ⚠️ Alerta Alto |
| Valor moderado | > R$ 500 em 24h | ⚠️ Alerta Médio |
| Frequência | ≥ 3 apostas em 24h | ⚠️ Alerta Médio |

### Resposta de Análise

```json
{
  "risk": true,
  "message": "Comportamento de risco detectado: 5 apostas totalizando R$ 250,00 nas últimas 24 horas.",
  "suggestion": "Com R$ 250,00, você poderia investir em um CDB que rende aproximadamente R$ 2,50 por mês com liquidez diária."
}
```

### Endpoints de Risco
```bash
# Analisar risco de um usuário
GET /api/bets/alerts/{userId}

# Verificar se pode apostar um valor
GET /api/bets/user/{userId}/can-bet?amount=150.00

# Obter estatísticas
GET /api/bets/user/{userId}/stats
```

---

## ✅ Conformidade SOLID

### Single Responsibility Principle (SRP)
- **Controllers**: Lidam apenas com requisições HTTP
- **Services**: Contêm toda a lógica de negócio
- **Repositories**: Acesso exclusivo a dados
- **DTOs**: Transferência de dados entre camadas
- **Mappers**: Conversão Entity ↔ DTO

### Open/Closed Principle (OCP)
- Interfaces `UserService`, `BetService`, `RiskAnalysisService`
- Implementações podem ser substituídas sem afetar controllers
- Fácil adicionar novos tipos de apostas ou análises

### Liskov Substitution Principle (LSP)
- Todas as implementações seguem contratos das interfaces
- `BetServiceImpl` substitui `BetService` sem quebrar código
- DTOs seguem padrão consistente

### Interface Segregation Principle (ISP)
- `BetService` com métodos específicos de apostas
- `UserService` com métodos específicos de usuários
- `RiskAnalysisService` dedicada à análise de risco
- Controllers não dependem de interfaces desnecessárias

### Dependency Inversion Principle (DIP)
- Injeção de dependências via Spring (@Autowired, constructor injection)
- Controllers dependem de interfaces (Services), não implementações
- Services dependem de abstrações (Repositories)

### Exemplo de DIP no Código
```java
// ✅ Bom - Depende de interface
@RestController
public class BetController {
    private final BetService betService; // Interface
    public BetController(BetService betService) {
        this.betService = betService;
    }
}

// ❌ Ruim - Depende de implementação
public class BetController {
    private BetServiceImpl betService; // Implementação
}
```

---

## 🔒 Segurança

### Configuração de Segurança

1. **Stateless JWT Authentication**
    - Sem sessões no servidor
    - Token auto-contido com informações do usuário
    - Assinado com chave secreta (HS512)

2. **Proteção CSRF Desativada**
    - APIs REST não necessitam (stateless)
    - Proteção via JWT é suficiente

3. **Filtro JWT Customizado**
   ```java
   JwtAuthenticationFilter valida cada requisição protegida
   ```

4. **Endpoints Públicos**
    - `/auth/login` - Para obter token
    - `/swagger-ui/**` - Documentação
    - `/v3/api-docs/**` - Especificação OpenAPI

5. **Endpoints Protegidos**
    - `/api/users/**` - Requer token
    - `/api/bets/**` - Requer token
    - `/auth/validate` - Requer token
    - `/auth/refresh` - Requer token

### Recomendações de Produção

```properties
# 1. Usar variáveis de ambiente para secrets
spring.security.jwt.secret=${JWT_SECRET}

# 2. Aumentar tempo de expiração do token
# Atual: 5 horas (18000 segundos)
# Produção: 1-2 horas

# 3. Usar HTTPS em todas as requisições
spring.security.require-https=true

# 4. Hash de senhas com BCrypt
# Implementar: BCryptPasswordEncoder.encode(password)

# 5. Rate limiting para /auth/login
# Implementar: Spring Cloud Gateway ou Zuul
```

---

## 📊 Banco de Dados

### Tabelas Principais

#### users
```sql
- id (PK)
- name (VARCHAR 100)
- email (VARCHAR 150, UNIQUE)
- age (INT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### auth_users
```sql
- id (PK)
- email (VARCHAR 255, UNIQUE)
- password (VARCHAR 255)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### bets
```sql
- id (PK)
- user_id (FK)
- amount (DECIMAL 10,2)
- type (VARCHAR 50) - SPORTS, CASINO, LOTTERY, POKER
- description (VARCHAR 500)
- status (VARCHAR 20) - PENDING, ACTIVE, WON, LOST, CANCELLED
- timestamp (TIMESTAMP)
```

### Índices para Performance
```sql
CREATE INDEX idx_bets_user_id ON bets(user_id);
CREATE INDEX idx_bets_status ON bets(status);
CREATE INDEX idx_bets_type ON bets(type);
CREATE INDEX idx_bets_timestamp ON bets(timestamp);
```

---

## 📚 Documentação Automática

### Acessar Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Acessar Especificação OpenAPI (JSON)
```
http://localhost:8080/v3/api-docs
```

### Acessar Especificação OpenAPI (YAML)
```
http://localhost:8080/v3/api-docs.yaml
```

### Importar no Postman
1. Abra Postman
2. `Ctrl+Shift+I` (Import)
3. Cole: `http://localhost:8080/v3/api-docs`
4. Clique "Continue" e "Import"

---

## 🐛 Troubleshooting

### Erro: "Connection refused" ao MySQL
```
Solução: Verifique se MySQL está rodando
Mac/Linux: mysql.server start
Windows: services.msc → MySQL80 → Start
```

### Erro: "Port 8080 already in use"
```
Solução: Mudar porta em application.properties
server.port=8081
```

### Erro: "Flyway migration failed"
```
Solução: Limpar e reexecutar
mvn clean flyway:clean flyway:migrate
```

### Erro: "JWT token expired"
```
Solução: Fazer refresh do token
POST /auth/refresh com o token antigo
```

### Erro: "Email already exists"
```
Solução: Usar email único
Verificar com GET /api/users/exists/email/{email}
```

---

## 🚀 Deploy

### Build para Produção
```bash
mvn clean package -DskipTests
```

### Executar JAR em Produção
```bash
java -jar target/consciousbet-1.0.0.jar \
  -Dspring.datasource.url=jdbc:mysql://prod-db:3306/consciousbet \
  -Dspring.datasource.username=prod_user \
  -Dspring.datasource.password=prod_password \
  -Dspring.security.jwt.secret=sua_chave_secreta_segura
```

### Docker (Opcional)
```dockerfile
FROM openjdk:17-jdk
COPY target/consciousbet-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
```

---

## 📞 Suporte

Para dúvidas ou problemas:
1. Consulte a documentação Swagger em `/swagger-ui.html`
2. Verifique os logs da aplicação
3. Abra uma issue no repositório GitHub

---

## 👨‍💻 Autores

- **Irana Pereira** – RM98593
- **Lucas Vinicius** – RM98480
- **Mariana Melo** – RM98121
- **Mateus Iago** – RM550270

**Instituição:** FIAP  
**Disciplina:** Arquitetura Orientada a Serviços  
**Professor:** Carlos Eduardo Machado  
**Data:** 2025

---

## 📄 Licença

Este projeto é acadêmico e está disponível sob licença MIT para fins educacionais.

---

## 🔄 Versionamento

- **v1.0.0** (Atual) - Release inicial com todas as funcionalidades base
    - Autenticação JWT
    - CRUD de Usuários e Apostas
    - Análise de Risco
    - Documentação Swagger
    - Testes automatizados