
# 🎰 Aposta Consciente XP

Projeto acadêmico desenvolvido com foco em arquitetura orientada a serviços (SOA), utilizando boas práticas de integração entre sistemas, autenticação com JWT e um módulo de análise de comportamento para apostas conscientes.

---

## 🚀 Tecnologias Utilizadas

- ✅ Java 17+
- ✅ Spring Boot 3+
- ✅ Spring Data JPA
- ✅ Spring Security + JWT
- ✅ Swagger/OpenAPI
- ✅ Postman (testes de requisição)
- ✅ Maven

---

## 📂 Estrutura do Projeto

```
src/
├── config         # Configurações de segurança e Swagger
├── controller     # Controladores REST
├── dto            # Objetos de transferência de dados (DTOs)
├── entity         # Entidades JPA
├── exception      # Tratamento de exceções customizadas
├── mapper         # Conversão entre Entity e DTO
├── repository     # Acesso ao banco (DAO)
├── security       # Filtro JWT e utilitários
├── service        # Interfaces de regras de negócio
├── serviceimpl    # Implementações das interfaces de serviço
```

---

## ⚙️ Como Executar

1. Clone o repositório:
```bash
git clonehttps://github.com/LucasVinicius45/Consciousbet
cd consciousbet
```

2. Execute a aplicação:
```bash
mvn spring-boot:run
```

3. Acesse no navegador:
- Swagger UI: http://localhost:8080/swagger-ui.html  

---

## 🔐 Autenticação JWT

### 1. Registrar Usuário
```http
POST /auth/register
Content-Type: application/json

{
  "email": "admin@email.com",
  "password": "123456",
  "name": "Admin",
  "age": 30
}
```

### 2. Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@email.com",
  "password": "123456"
}
```

### 3. Resposta
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "admin@email.com",
  "tokenType": "Bearer"
}
```

🔐 Copie o token e use no Swagger com o botão **"Authorize"**.

---

## 📡 Endpoints Principais

### 👤 Usuários (`/users`)
| Método | Endpoint        | Descrição             |
|--------|------------------|------------------------|
| POST   | /users           | Criar usuário          |
| GET    | /users           | Listar todos           |
| GET    | /users/{id}      | Buscar por ID          |
| PUT    | /users/{id}      | Atualizar usuário      |
| DELETE | /users/{id}      | Deletar usuário        |

### 🎲 Apostas (`/bets` / `/alerts`)
| Método | Endpoint          | Descrição                                   |
|--------|-------------------|---------------------------------------------|
| POST   | /bets             | Registrar aposta                             |
| GET    | /alerts/{userId}  | Verificar comportamento de risco + sugestão |

### 🔑 Autenticação (`/auth`)
| Método | Endpoint         | Descrição              |
|--------|------------------|--------------------------|
| POST   | /auth/login      | Login e geração de token |
| GET    | /auth/validate   | Validar token JWT        |
| POST   | /auth/refresh    | Gerar novo token         |

---

## 📘 Documentação da API

Acesse [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
A documentação está integrada com autenticação JWT via botão **"Authorize"**.

---

## 🧪 Testes

- ✅ Postman: Requisições de login, CRUD e alertas testadas
- ✅ Swagger: Testes diretos pela interface

---

## 🧠 Módulo de Apostas Conscientes

A lógica de `/alerts/{userId}` verifica o histórico de apostas e retorna:
- Nível de risco (`LOW`, `MODERATE`, `HIGH`)
- Sugestões financeiras (ex: investir em emergência, guardar valor, etc.)

---

## 📦 Versão da API

- `v1.0.0` – Entrega acadêmica completa com todos os requisitos implementados.

---

## 👨‍🏫 Autores

- **Irana Pereira** – RM98593
- **Lucas Vinicius** – RM98480 
- **Mariana Melo** – RM98121 
- **Mateus Iago** – RM550270
- **Instituição:** FIAP  
- **Disciplina:** Arquitetura Orientada a Serviços  
- **Carlos Eduardo Machado:** Nome do Professor  
