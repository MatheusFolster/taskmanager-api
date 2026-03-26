# Task Manager API — Product Requirements Document

**Data:** 2026-03-26
**Status:** Draft

---

## Overview

API REST para gestão de tarefas pessoais com hierarquia de Projetos → Tarefas → Subtarefas. Projeto de portfólio focado em demonstrar arquitetura Spring Boot profissional, qualidade de código e cobertura de testes.

---

## Personas

### Persona 1: O Desenvolvedor (usuário da API)

> Desenvolvedor intermediário que usa a API via frontend Angular ou Postman/Swagger. Quer um sistema que funcione de forma previsível, com respostas claras e tratamento correto de erros.

### Persona 2: O Avaliador (tech lead / recrutador)

> Avalia o repositório GitHub. Lê o código, verifica a estrutura de pastas, olha os testes e o README. Quer ver decisões técnicas justificadas e código limpo.

---

## User Stories

### Autenticação

- Como usuário, quero me registrar com email e senha para ter acesso à API
- Como usuário, quero fazer login e receber um JWT para autenticar minhas requisições
- Como usuário, quero que minhas rotas sejam protegidas e inacessíveis sem token válido

### Projetos

- Como usuário, quero criar um projeto com nome e descrição para organizar minhas tarefas
- Como usuário, quero listar todos os meus projetos para ter uma visão geral
- Como usuário, quero atualizar nome e descrição de um projeto para mantê-lo atualizado
- Como usuário, quero arquivar um projeto para removê-lo da visão principal sem deletar
- Como usuário, quero deletar um projeto e todas as suas tarefas associadas

### Tarefas

- Como usuário, quero criar uma tarefa dentro de um projeto com título, descrição, prioridade e data de vencimento
- Como usuário, quero listar tarefas de um projeto com filtros por status, prioridade e tags
- Como usuário, quero atualizar os dados de uma tarefa
- Como usuário, quero mover o status de uma tarefa (TODO → IN_PROGRESS → DONE)
- Como usuário, quero adicionar tags a uma tarefa para categorização
- Como usuário, quero deletar uma tarefa

### Subtarefas

- Como usuário, quero criar subtarefas dentro de uma tarefa para detalhar o trabalho
- Como usuário, quero listar subtarefas de uma tarefa
- Como usuário, quero marcar uma subtarefa como concluída
- Como usuário, quero deletar uma subtarefa

---

## Requisitos Funcionais

### RF01 — Autenticação e Autorização

| ID | Requisito |
|---|---|
| RF01.1 | Registro de usuário com email, nome e senha (hash bcrypt) |
| RF01.2 | Login retorna JWT com expiração configurável |
| RF01.3 | Refresh token para renovação sem novo login |
| RF01.4 | Todas as rotas (exceto /auth/**) exigem JWT válido |
| RF01.5 | Usuário só acessa seus próprios recursos |

### RF02 — Projetos

| ID | Requisito |
|---|---|
| RF02.1 | CRUD completo de projetos |
| RF02.2 | Projeto tem: id, nome, descrição, status (ACTIVE/ARCHIVED), createdAt, updatedAt |
| RF02.3 | Listar projetos com paginação |
| RF02.4 | Filtrar projetos por status |
| RF02.5 | Deletar projeto faz cascade delete nas tarefas e subtarefas |

### RF03 — Tarefas

| ID | Requisito |
|---|---|
| RF03.1 | CRUD completo de tarefas |
| RF03.2 | Tarefa tem: id, título, descrição, status, prioridade, dueDate, tags, createdAt, updatedAt |
| RF03.3 | Status: TODO, IN_PROGRESS, DONE, CANCELLED |
| RF03.4 | Prioridade: LOW, MEDIUM, HIGH, URGENT |
| RF03.5 | Tags: lista de strings livres |
| RF03.6 | Listar tarefas com filtros (status, prioridade, tag) e paginação |
| RF03.7 | Ordenação por dueDate, prioridade ou createdAt |

### RF04 — Subtarefas

| ID | Requisito |
|---|---|
| RF04.1 | CRUD completo de subtarefas |
| RF04.2 | Subtarefa tem: id, título, concluída (boolean), createdAt |
| RF04.3 | Subtarefa pertence a uma tarefa |
| RF04.4 | Progresso da tarefa calculado pelo % de subtarefas concluídas |

---

## Requisitos Não-Funcionais

| ID | Requisito |
|---|---|
| RNF01 | Senhas armazenadas com BCrypt (fator ≥ 12) |
| RNF02 | JWT com expiração de 24h (configurável via properties) |
| RNF03 | Respostas de erro padronizadas (timestamp, status, message, path) |
| RNF04 | Paginação padrão: 20 itens por página |
| RNF05 | Cobertura de testes ≥ 80% nas camadas Service e Controller |
| RNF06 | Documentação Swagger/OpenAPI disponível em /swagger-ui.html |
| RNF07 | Logs estruturados com nível INFO em produção |
| RNF08 | Variáveis sensíveis via application.properties (não hardcoded) |

---

## Arquitetura e Stack

```
[Angular Frontend]
        ↓ HTTP + JWT
[Spring Boot API]
   ↓           ↓
[Spring Security]  [Controllers]
                       ↓
                   [Services]
                       ↓
                   [Repositories]
                       ↓
                 [PostgreSQL / H2]
```

**Stack:**
- Java 21 + Spring Boot 3.x
- Spring Security + JWT (jjwt)
- Spring Data JPA + Hibernate
- PostgreSQL (prod) / H2 (testes)
- MapStruct (conversão DTO ↔ Entity)
- JUnit 5 + Mockito + TestContainers
- Swagger/OpenAPI 3

**Estrutura de Pacotes:**
```
com.taskmanager
├── config/
├── controller/
├── service/
│   └── impl/
├── repository/
├── entity/
├── dto/
├── mapper/
├── exception/
├── security/
└── util/
```

---

## Edge Cases e Tratamento de Erros

| Cenário | Comportamento Esperado |
|---|---|
| Login com senha errada | 401 com mensagem genérica (não revela se email existe) |
| Token expirado | 401 com mensagem "Token expired" |
| Acessar projeto de outro usuário | 403 Forbidden |
| Projeto não encontrado | 404 com mensagem descritiva |
| Deletar projeto com tarefas | Cascade delete — remove tudo |
| Criar tarefa com dueDate no passado | Aceita (sem validação de negócio, apenas formato) |
| Tag duplicada na mesma tarefa | Deduplica silenciosamente |
| Subtarefa em tarefa DONE | Permite (subtarefa pode ser adicionada a qualquer status) |

---

## Critérios de Aceitação por Feature

### Auth
- [ ] POST /auth/register cria usuário e retorna 201
- [ ] POST /auth/login retorna JWT válido
- [ ] Rota protegida sem token retorna 401
- [ ] Rota protegida com token inválido retorna 401

### Projetos
- [ ] GET /projects retorna apenas projetos do usuário logado
- [ ] POST /projects cria projeto e retorna 201 com Location header
- [ ] DELETE /projects/{id} remove projeto e todas as tarefas filhas

### Tarefas
- [ ] GET /projects/{id}/tasks com filtro ?status=TODO retorna apenas tarefas com esse status
- [ ] PATCH /tasks/{id}/status atualiza apenas o status
- [ ] GET /projects/{id}/tasks retorna progresso calculado por subtarefas

### Subtarefas
- [ ] PATCH /subtasks/{id}/complete marca como concluída e retorna progresso atualizado da tarefa pai
