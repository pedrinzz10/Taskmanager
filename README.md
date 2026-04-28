# 📝 Task Manager API

## 📌 Visão Geral

API REST para gerenciamento de tarefas, desenvolvida para demonstrar proficiência em Java 21, persistência relacional e containerização. Ideal como base para estudos de DevOps e arquitetura de microsserviços.

---

## 🚀 Funcionalidades

- **CRUD Completo:** Criação, listagem, atualização e exclusão de tarefas.
- **Persistência:** Integração com MySQL via Spring Data JPA.
- **Containerização:** Ambiente completo (App + DB) orquestrado via Docker Compose.

---

## 🏗️ Estrutura do Projeto

O projeto segue uma **Layered Architecture** (Arquitetura em Camadas) para garantir a separação de responsabilidades:

```
src/main/java/com/example/taskmanager
├── config/      # Configurações da aplicação
├── controller/  # Camada de exposição (Endpoints REST)
├── dto/         # Objetos de transferência de dados
├── model/       # Entidades de domínio (Task)
├── repository/  # Interface de comunicação com o Banco de Dados
└── service/     # Regras de negócio e lógica da aplicação
```

---

## 🐳 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados.

### Passo a Passo

**Subir os containers (o build acontece dentro do Docker):**
```bash
docker-compose up --build
```

---

## 🌐 API

**Base URL:** `http://localhost:8080`

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

- API pública sem autenticação.
- Ausência de tratamento global de exceções.
- Falta de validação de campos (`@Valid`).
- Sem paginação nos endpoints de listagem.

---

## 📈 Roadmap de Melhorias (Sugestões para AI Review)

Para elevar o nível do projeto, os seguintes pontos devem ser abordados:

- **Segurança:** Implementar autenticação via JWT (JSON Web Token) com criptografia de senhas BCrypt.
- **Qualidade de Código:** Introduzir MapStruct para conversão de DTOs e interfaces nos Services.
- **Resiliência:** Adicionar `@ControllerAdvice` para capturar erros de forma amigável.
- **Banco de Dados:** Implementar migrações com Flyway ou Liquibase.
- **DevOps:** Configurar pipeline CI/CD para build e deploy automático nas VMs.

---

## 👨‍💻 Autor

**Pedro Henrique Oliveira**
Foco em Backend, DevOps e arquiteturas escaláveis.