# 📝 Task Manager

## 📌 Visão Geral

Aplicação completa de gerenciamento de tarefas com **API REST** em Java e **frontend web**, totalmente containerizada via Docker. Desenvolvida para demonstrar proficiência em Java 21, Spring Boot, persistência relacional, segurança, containerização e deploy em nuvem.

---

## 🏗️ Arquitetura

A aplicação é composta por três serviços orquestrados via Docker Compose:

```
Navegador
    │
    ▼
[ Nginx — porta 80 ]          ← Frontend (HTML + Bootstrap)
    │
    ├── GET /          → serve index.html
    └── /tasks/**      → proxy reverso
                            │
                            ▼
                   [ Spring Boot — porta 8080 ]   ← API REST
                            │
                            ▼
                      [ MySQL 8 — porta 3306 ]    ← Banco de dados
```

O Nginx atua como **reverse proxy**: serve o frontend e encaminha todas as chamadas `/tasks` para a API Spring Boot internamente. O professor (ou qualquer usuário) acessa apenas a **porta 80** — sem precisar conhecer a porta da API.

---

## 🚀 Tecnologias

| Camada | Tecnologia |
|--------|------------|
| Backend | Java 21 + Spring Boot 3.4.5 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de Dados | MySQL 8 |
| Segurança | Spring Security (Basic Auth + BCrypt) |
| Frontend | HTML5 + Bootstrap 5 + JavaScript (Fetch API) |
| Servidor Web | Nginx (Alpine) |
| Build | Gradle (multi-stage Docker build) |
| Orquestração | Docker Compose |

---

## 📁 Estrutura do Projeto

```
taskmanager/
├── src/main/java/com/example/taskmanager/
│   ├── config/       # SecurityConfig — autenticação e filtros
│   ├── controller/   # TaskController — endpoints REST
│   ├── dto/          # TaskRequest, TaskResponse
│   ├── model/        # Entidade Task (JPA)
│   ├── repository/   # TaskRepository (Spring Data)
│   └── service/      # TaskService — regras de negócio
├── src/main/resources/
│   └── application.properties
├── frontend/
│   ├── index.html    # Interface web completa
│   ├── nginx.conf    # Proxy reverso + servidor estático
│   └── Dockerfile    # nginx:alpine
├── Dockerfile        # Multi-stage build da API (JDK → JRE)
├── docker-compose.yml
└── build.gradle
```

---

## 🐳 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados.

### Subir tudo com um comando

```bash
docker compose up --build -d
```

O build do projeto Java acontece **dentro do Docker** — não é necessário ter Java ou Gradle instalado na máquina.

### Verificar se está rodando

```bash
docker ps
```

Deve exibir três containers ativos:

| Container | Imagem | Porta |
|-----------|--------|-------|
| `task_frontend` | nginx:alpine | 80 |
| `task_api` | eclipse-temurin:21-jre | 8080 |
| `mysql_tasks` | mysql:8 | 3306 |

### Parar os containers

```bash
docker compose down
```

---

## 🌐 Acesso

| Recurso | URL |
|---------|-----|
| Frontend (interface web) | `http://localhost` |
| API REST | `http://localhost:8080/tasks` |

---

## 🔐 Autenticação

A API utiliza **HTTP Basic Auth**. Todas as rotas exigem credenciais.

| Campo | Valor |
|-------|-------|
| Usuário | `admin` |
| Senha | `123` |

A senha é armazenada com hash **BCrypt** (nunca em texto plano).

---

## 🖥️ Frontend

Acesse `http://localhost` no navegador. A tela de login aparece automaticamente.

**Funcionalidades disponíveis:**

- Login com usuário e senha
- Listagem de todas as tarefas
- Criação de nova tarefa (título + descrição)
- Edição de tarefa existente
- Marcar tarefa como concluída (checkbox)
- Exclusão de tarefa (com confirmação)

---

## 📬 API — Endpoints

**Base URL:** `http://localhost:8080`

**Header obrigatório em todas as requisições:**
```
Authorization: Basic YWRtaW46MTIz
```
> `YWRtaW46MTIz` é o Base64 de `admin:123`

---

### GET `/tasks` — Listar todas as tarefas

```bash
curl -u admin:123 http://localhost:8080/tasks
```

**Resposta 200:**
```json
[
  {
    "id": 1,
    "title": "Estudar Spring Boot",
    "description": "Finalizar módulo de segurança",
    "completed": false
  },
  {
    "id": 2,
    "title": "Configurar Docker",
    "description": "Multi-stage build",
    "completed": true
  }
]
```

---

### GET `/tasks/{id}` — Buscar tarefa por ID

```bash
curl -u admin:123 http://localhost:8080/tasks/1
```

**Resposta 200:**
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Finalizar módulo de segurança",
  "completed": false
}
```

**Resposta 404:** tarefa não encontrada.

---

### POST `/tasks` — Criar tarefa

```bash
curl -u admin:123 \
  -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Nova tarefa", "description": "Descrição aqui", "completed": false}'
```

**Body:**
```json
{
  "title": "Nova tarefa",
  "description": "Descrição aqui",
  "completed": false
}
```

**Resposta 200:**
```json
{
  "id": 3,
  "title": "Nova tarefa",
  "description": "Descrição aqui",
  "completed": false
}
```

---

### PUT `/tasks/{id}` — Atualizar tarefa

```bash
curl -u admin:123 \
  -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Tarefa atualizada", "description": "Nova descrição", "completed": true}'
```

**Body:**
```json
{
  "title": "Tarefa atualizada",
  "description": "Nova descrição",
  "completed": true
}
```

**Resposta 200:** retorna a tarefa atualizada.
**Resposta 404:** tarefa não encontrada.

---

### DELETE `/tasks/{id}` — Remover tarefa

```bash
curl -u admin:123 -X DELETE http://localhost:8080/tasks/1
```

**Resposta 204:** sem corpo.
**Resposta 404:** tarefa não encontrada.

---

## ☁️ Deploy na Azure VM

> 📄 **Guia completo para teste na vm:** [`DEPLOY.md`](./DEPLOY.md)


### Pré-requisitos na VM

- Ubuntu 22.04 LTS
- Docker e Docker Compose instalados
- Portas **80** e **8080** abertas no NSG (Network Security Group)

### Passos

**1. Instalar Docker na VM:**
```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo usermod -aG docker $USER && newgrp docker
```

**2. Clonar o repositório:**
```bash
git clone https://github.com/pedrinzz10/Taskmanager.git
cd Taskmanager
```

**3. Subir os containers:**
```bash
docker compose up --build -d
```

**4. Verificar:**
```bash
docker ps
curl -u admin:123 http://localhost:8080/tasks
```

**5. Acessar de fora da VM:**

| Recurso | URL |
|---------|-----|
| Frontend | `http://20.110.177.253` |
| API | `http://20.110.177.253:8080/tasks` |

---

## ⚠️ Limitações Atuais

- Usuário em memória — credenciais não persistidas em banco.
- Ausência de tratamento global de exceções.
- Sem paginação nos endpoints de listagem.
- MySQL exposto na porta 3306 (desabilitar em produção real).

---

## 📈 Roadmap de Melhorias

- **Segurança:** Evoluir de Basic Auth para JWT com refresh token.
- **Qualidade:** Introduzir MapStruct para mapeamento de DTOs.
- **Resiliência:** Adicionar `@ControllerAdvice` para tratamento de erros.
- **Banco de Dados:** Implementar migrações com Flyway ou Liquibase.
- **DevOps:** Configurar pipeline CI/CD para build e deploy automático.

---

## 👨‍💻 Autor

**Pedro Henrique Oliveira**
Foco em Backend, DevOps e arquiteturas escaláveis.
