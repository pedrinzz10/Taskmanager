# 🚀 Guia de Deploy — Azure VM

Este guia descreve como executar a aplicação **Task Manager** em uma VM na Azure do zero.

> **Pré-requisito:** ter uma conta ativa na Azure com uma VM Ubuntu 22.04 criada.

---

## 📋 Índice

1. [Criar a VM na Azure](#1-criar-a-vm-na-azure)
2. [Abrir as portas no firewall](#2-abrir-as-portas-no-firewall)
3. [Conectar à VM via SSH](#3-conectar-à-vm-via-ssh)
4. [Instalar o Docker](#4-instalar-o-docker)
5. [Clonar o repositório](#5-clonar-o-repositório)
6. [Subir a aplicação](#6-subir-a-aplicação)
7. [Verificar se está funcionando](#7-verificar-se-está-funcionando)
8. [Acessar a aplicação](#8-acessar-a-aplicação)

---

## 1. Criar a VM na Azure

Acesse [portal.azure.com](https://portal.azure.com) e siga os passos:

```
Menu lateral → Máquinas Virtuais → Criar → Máquina Virtual do Azure
```

Preencha os campos conforme a tabela abaixo:

| Campo | Valor recomendado |
|-------|-------------------|
| Grupo de recursos | `taskmanager-rg` (criar novo) |
| Nome da VM | `taskmanager-vm` |
| Região | Brazil South (ou East US) |
| Imagem | Ubuntu Server 22.04 LTS |
| Tamanho | Standard_B2s (2 vCPUs, 4 GB RAM) |
| Tipo de autenticação | Chave pública SSH |
| Nome de usuário | `azureuser` |
| Portas de entrada | Permitir SSH (22) |

Clique em **Revisar + Criar** → **Criar**.

Após a criação, acesse a VM e copie o **Endereço IP Público** — ele será usado em todos os passos seguintes.

---

## 2. Abrir as portas no firewall

A Azure bloqueia todas as portas por padrão. É necessário abrir as portas **80** (frontend) e **8080** (API).

```
VM → Rede → Regras de porta de entrada → Adicionar regra de porta de entrada
```

Adicione **duas regras**, uma de cada vez:

**Regra 1 — Frontend:**

| Campo | Valor |
|-------|-------|
| Intervalos de porta de destino | `80` |
| Protocolo | TCP |
| Ação | Permitir |
| Prioridade | `1000` |
| Nome | `Allow-HTTP` |

**Regra 2 — API:**

| Campo | Valor |
|-------|-------|
| Intervalos de porta de destino | `8080` |
| Protocolo | TCP |
| Ação | Permitir |
| Prioridade | `1010` |
| Nome | `Allow-API` |

---

## 3. Conectar à VM via SSH

Abra o terminal do seu computador e execute:

```bash
ssh azureuser@20.110.177.253
```

Na primeira conexão, confirme digitando `yes` quando solicitado.

---

## 4. Instalar o Docker

Execute os comandos abaixo **dentro da VM** (via SSH), um bloco de cada vez:

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release
```

```bash
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
  sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg
```

```bash
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

```bash
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

```bash
sudo usermod -aG docker azureuser
newgrp docker
```

Verifique se a instalação funcionou:

```bash
docker --version
docker compose version
```

Saída esperada (versões podem variar):
```
Docker version 26.x.x
Docker Compose version v2.x.x
```

---

## 5. Clonar o repositório

Ainda dentro da VM, clone o projeto do GitHub:

```bash
git clone https://github.com/pedrinzz10/Taskmanager.git
cd Taskmanager
```

Confirme que os arquivos estão presentes:

```bash
ls
```

A saída deve incluir: `Dockerfile`, `docker-compose.yml`, `src/`, `frontend/`, `build.gradle`.

---

## 6. Subir a aplicação

Execute o comando abaixo para construir e iniciar todos os containers:

```bash
docker compose up --build -d
```

> O primeiro build pode levar **3 a 5 minutos** — o Docker baixa as dependências do projeto Java.

Acompanhe os logs até a aplicação inicializar completamente:

```bash
docker compose logs -f app
```

Aguarde a mensagem:
```
Started TaskmanagerApplication in X.XXX seconds
```

Pressione `Ctrl + C` para sair dos logs.

---

## 7. Verificar se está funcionando

Confirme que os três containers estão ativos:

```bash
docker ps
```

Saída esperada:

```
CONTAINER ID   IMAGE            STATUS          PORTS
xxxxxxxxxxxx   task_frontend    Up X minutes    0.0.0.0:80->80/tcp
xxxxxxxxxxxx   task_api         Up X minutes    0.0.0.0:8080->8080/tcp
xxxxxxxxxxxx   mysql:8          Up X minutes (healthy)
```

Teste a API de dentro da VM:

```bash
curl -u admin:123 http://localhost:8080/tasks
```

Resposta esperada: `[]` (lista vazia — ainda sem tarefas cadastradas).

---

## 8. Acessar a aplicação

Com os containers rodando, acesse pelo navegador:

| Recurso | URL |
|---------|-----|
| **Interface Web** | `http://20.110.177.253` |
| **API REST** | `http://20.110.177.253:8080/tasks` |

### Login na interface web

Abra `http://20.110.177.253` no navegador e utilize:

| Campo | Valor |
|-------|-------|
| Usuário | `admin` |
| Senha | `123` |

### Funcionalidades disponíveis

- Listar todas as tarefas
- Criar nova tarefa
- Editar tarefa existente
- Marcar tarefa como concluída
- Deletar tarefa

---

## 🔁 Reiniciar após reboot da VM

Os containers estão configurados com `restart: always` — eles sobem automaticamente quando a VM reinicia, sem nenhuma intervenção manual.

Para reiniciar manualmente:

```bash
docker compose restart
```

---

## 🛠️ Solução de Problemas

**Navegador não abre / timeout:**
- Verifique se as regras de porta 80 e 8080 foram criadas no NSG (Passo 2)
- Confirme que os containers estão rodando: `docker ps`

**Container da API reiniciando em loop:**
```bash
docker compose logs app
```
Aguarde o MySQL ficar saudável — o app reinicia automaticamente até conseguir conectar.

**Permissão negada no Docker:**
```bash
newgrp docker
```

**Verificar espaço em disco:**
```bash
df -h
```

---

## 📦 Resumo dos Serviços

| Serviço | Container | Porta | Descrição |
|---------|-----------|-------|-----------|
| Frontend | `task_frontend` | 80 | Interface web (Nginx + HTML) |
| API | `task_api` | 8080 | Spring Boot REST API |
| Banco | `mysql_tasks` | 3306 | MySQL 8 (interno) |
