# 📝 Task Manager API

## 📌 Visão Geral

API REST robusta para gerenciamento de tarefas, desenvolvida para demonstrar proficiência em Java 21, persistência relacional, segurança básica e containerização. Ideal como base para estudos de DevOps e arquitetura de microsserviços.

---

## 🚀 Funcionalidades

- **CRUD Completo:** Criação, listagem, atualização e exclusão de tarefas.
- **Persistência:** Integração com MySQL via Spring Data JPA.
- **Segurança:** Autenticação via Spring Security.
- **Containerização:** Ambiente completo (App + DB) orquestrado via Docker Compose.

---

## 🏗️ Estrutura do Projeto

O projeto segue uma **Layered Architecture** (Arquitetura em Camadas) para garantir a separação de responsabilidades:

```
src/main/java/com/example/taskmanager
├── config/      # Configurações de segurança e inicialização
├── controller/  # Camada de exposição (Endpoints REST)
├── dto/         # Objetos de transferência de dados
├── model/       # Entidades de domínio (Task, User)
├── repository/  # Interface de comunicação com o Banco de Dados
└── service/     # Regras de negócio e lógica da aplicação
```

---

## 🐳 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados.

### Passo a Passo

**Build do projeto:**
```bash
./gradlew clean build -x test
```

**Subir os containers:**
```bash
docker-compose up --build
```

---

## 🌐 API

**Base URL:** `http://localhost:8080`

---

## 🔐 Segurança e Acesso

A API utiliza **Basic Auth**. Um usuário padrão é gerado automaticamente na inicialização:

- **Usuário:** `admin`
- **Senha:** `123`

---

## 📬 Documentação da API (Endpoints)

| Método | Endpoint      | Descrição                     |
|--------|---------------|-------------------------------|
| GET    | /tasks        | Lista todas as tarefas        |
| GET    | /tasks/{id}   | Busca tarefa por ID           |
| POST   | /tasks        | Cria uma nova tarefa          |
| PUT    | /tasks/{id}   | Atualiza uma tarefa existente |
| DELETE | /tasks/{id}   | Remove uma tarefa             |

### Exemplo de Payload (POST/PUT)

```json
{
  "title": "Estudar Spring Boot",
  "description": "Finalizar o módulo de Docker e Segurança",
  "completed": false
}
```

---

## ⚠️ Limitações Atuais

- Senhas armazenadas em texto plano.
- Ausência de tratamento global de exceções.
- Falta de validação de campos (`@Valid`).
- Sem paginação nos endpoints de listagem.

---

## 📈 Roadmap de Melhorias (Sugestões para AI Review)

Para elevar o nível do projeto, os seguintes pontos devem ser abordados:

- **Segurança:** Implementar JWT (JSON Web Token) e criptografia de senhas com BCrypt.
- **Qualidade de Código:** Introduzir MapStruct para conversão de DTOs e interfaces nos Services.
- **Resiliência:** Adicionar `@ControllerAdvice` para capturar erros de forma amigável.
- **Banco de Dados:** Implementar migrações com Flyway ou Liquibase.
- **DevOps:** Otimizar o Dockerfile com Multi-stage build para reduzir o tamanho da imagem final.

---

## 👨‍💻 Autor

**Pedro Henrique Oliveira**
Foco em Backend, DevOps e arquiteturas escaláveis.