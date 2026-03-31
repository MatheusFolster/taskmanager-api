# Task Manager API

API REST para gestão de tarefas pessoais com hierarquia **Projetos → Tarefas → Subtarefas**. Projeto de portfólio focado em demonstrar arquitetura Spring Boot profissional, código limpo e cobertura de testes.

---

## Visão Geral

A API permite que um usuário autenticado organize seu trabalho em **Projetos**, desmembre cada projeto em **Tarefas** (com status, prioridade, tags e data de vencimento) e detalhe cada tarefa com **Subtarefas**, que alimentam um indicador de progresso de conclusão.

Todas as rotas são protegidas por **JWT**. Cada usuário acessa apenas os seus próprios recursos.

---

## Arquitetura

```
┌─────────────────────────────────────────────┐
│          Cliente HTTP / Swagger UI           │
└─────────────────────┬───────────────────────┘
                      │ HTTP + JWT
┌─────────────────────▼───────────────────────┐
│              Spring Boot API                 │
│                                             │
│  JwtAuthenticationFilter                    │
│           │                                 │
│  ┌────────▼──────────────────────────────┐  │
│  │           Controllers                 │  │
│  │  AuthController  ProjectController    │  │
│  │  TaskController  SubtaskController    │  │
│  └────────────────────┬──────────────────┘  │
│                       │                     │
│  ┌────────────────────▼──────────────────┐  │
│  │             Services                  │  │
│  │  AuthService  ProjectService          │  │
│  │  TaskService  SubtaskService          │  │
│  └────────────────────┬──────────────────┘  │
│                       │                     │
│  ┌────────────────────▼──────────────────┐  │
│  │           Repositories                │  │
│  │  UserRepo  ProjectRepo  TaskRepo      │  │
│  │  RefreshTokenRepo  SubtaskRepo        │  │
│  └────────────────────┬──────────────────┘  │
│                       │                     │
└───────────────────────┼─────────────────────┘
                        │
          ┌─────────────▼─────────────┐
          │  PostgreSQL / H2 (testes) │
          └───────────────────────────┘
```

### Estrutura de Pacotes

```
com.taskmanager
├── config/          # SecurityConfig, OpenApiConfig, JwtProperties
├── controller/      # Controllers REST
├── service/
│   └── impl/        # Regras de negócio
├── repository/      # Interfaces Spring Data JPA
├── entity/
│   └── enums/       # ProjectStatus, TaskStatus, TaskPriority
├── dto/
│   ├── auth/        # RegisterRequest, LoginRequest, AuthResponse...
│   ├── project/     # CreateProjectRequest, ProjectResponse...
│   ├── task/        # CreateTaskRequest, TaskResponse...
│   └── subtask/     # CreateSubtaskRequest, SubtaskResponse
├── mapper/          # Interfaces MapStruct
├── exception/       # GlobalExceptionHandler, ApiError, exceções customizadas
├── security/        # JwtUtil, JwtAuthenticationFilter, UserDetailsServiceImpl
└── util/            # TaskSpecification
```

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.4.4 |
| Segurança | Spring Security + JWT (jjwt 0.12.6) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL (produção) / H2 (testes) |
| Migrações | Flyway |
| Mapeamento | MapStruct 1.6.3 |
| Boilerplate | Lombok |
| Documentação | Swagger / OpenAPI 3 (springdoc 2.8.4) |
| Testes | JUnit 5 + Mockito + MockMvc |

---

## Status do Projeto

### ✅ Fase 1 — Autenticação (concluída)

- `POST /auth/register` — cria conta de usuário e retorna JWT
- `POST /auth/login` — valida credenciais e retorna JWT
- `POST /auth/refresh` — renova o access token via refresh token
- Senha armazenada com BCrypt (fator 12)
- Autenticação JWT stateless (access token 24h, refresh token 7d)
- Todas as rotas protegidas exceto `/auth/**` e `/swagger-ui/**`
- Respostas de erro padronizadas com `timestamp`, `status`, `message` e `path`

### ✅ Fase 2 — Projetos e Tarefas (concluída)

**Projetos**

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/projects` | Lista projetos com paginação e filtro opcional `?status=` |
| `POST` | `/projects` | Cria projeto (201 + header Location) |
| `GET` | `/projects/{id}` | Busca projeto por ID |
| `PUT` | `/projects/{id}` | Atualiza nome e descrição |
| `PATCH` | `/projects/{id}/archive` | Arquiva projeto (status → ARCHIVED) |
| `DELETE` | `/projects/{id}` | Remove projeto e todas as tarefas (cascade) |

**Tarefas**

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/projects/{id}/tasks` | Lista tarefas com filtros `?status=`, `?priority=`, `?tag=` e paginação |
| `POST` | `/projects/{id}/tasks` | Cria tarefa (201 + header Location) |
| `GET` | `/tasks/{id}` | Busca tarefa por ID |
| `PUT` | `/tasks/{id}` | Atualiza todos os campos da tarefa |
| `PATCH` | `/tasks/{id}/status` | Atualiza apenas o status |
| `DELETE` | `/tasks/{id}` | Remove tarefa |

**Campos da tarefa:** título, descrição, status (`TODO` / `IN_PROGRESS` / `DONE` / `CANCELLED`), prioridade (`LOW` / `MEDIUM` / `HIGH` / `URGENT`), dueDate, tags (conjunto livre de strings — deduplicado automaticamente).

### ✅ Fase 3 — Subtarefas (concluída)

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/tasks/{id}/subtasks` | Lista subtarefas em ordem de criação |
| `POST` | `/tasks/{id}/subtasks` | Cria subtarefa (201 + header Location) |
| `PATCH` | `/subtasks/{id}/complete` | Marca subtarefa como concluída |
| `DELETE` | `/subtasks/{id}` | Remove subtarefa |

**Progresso da tarefa:** o campo `progress` no `TaskResponse` retorna o percentual de subtarefas concluídas (0 quando não há subtarefas).

### 🔲 Fase 4 — Qualidade e Cobertura

- Cobertura de testes ≥ 80% nas camadas Service e Controller
- Refatoração e revisão de código
- Código limpo sem warnings no SonarLint / Checkstyle

### 🔲 Fase 5 — Swagger + README

- Anotações Swagger completas em todos os endpoints
- Diagrama de entidades
- Documentação das decisões de arquitetura

### 🔲 Fase 6 — Opcional / Nice to Have

- Docker Compose (API + PostgreSQL)
- Pipeline de CI com GitHub Actions
- Frontend em Angular + Tailwind + PrimeNG

---

## Relacionamento entre Entidades

```
User
 └── Project (1:N)
      └── Task (1:N)
           ├── tags: Set<String>  (@ElementCollection)
           └── Subtask (1:N)
```

---

## Executando Localmente

### Pré-requisitos

- Java 21
- Maven 3.9+
- PostgreSQL 15+ rodando em `localhost:5432`

### 1. Criar o banco de dados

```sql
CREATE DATABASE taskmanager;
```

### 2. Configurar credenciais

A configuração padrão em `application.properties` usa:

```
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Para sobrescrever, use variáveis de ambiente:

```bash
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
export JWT_SECRET=sua_chave_base64   # mínimo 256 bits
```

### 3. Subir a aplicação

```bash
./mvnw spring-boot:run
```

O Flyway executa as migrations automaticamente na inicialização.

### 4. Acessar o Swagger UI

```
http://localhost:8080/swagger-ui.html
```

Use `POST /auth/register` para criar uma conta, `POST /auth/login` para obter o token, depois clique em **Authorize** e cole o token.

---

## Executando os Testes

Os testes utilizam banco **H2 em memória** — nenhum PostgreSQL é necessário.

```bash
./mvnw test
```

Cobertura atual: **63 testes** em 8 classes de teste (unitários + integração).

| Classe de teste | Tipo | Testes |
|---|---|---|
| `AuthServiceTest` | Unitário (Mockito) | 8 |
| `ProjectServiceTest` | Unitário (Mockito) | 8 |
| `TaskServiceTest` | Unitário (Mockito) | 6 |
| `SubtaskServiceTest` | Unitário (Mockito) | 8 |
| `AuthControllerTest` | Integração (MockMvc) | 9 |
| `ProjectControllerTest` | Integração (MockMvc) | 10 |
| `TaskControllerTest` | Integração (MockMvc) | 7 |
| `SubtaskControllerTest` | Integração (MockMvc) | 7 |

---

## Respostas de Erro

Todos os erros seguem um formato consistente:

```json
{
  "timestamp": "2026-03-26T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with id: 42",
  "path": "/projects/42"
}
```

| Cenário | HTTP Status |
|---|---|
| Credenciais inválidas | 401 |
| Token expirado | 401 |
| Acessar recurso de outro usuário | 403 |
| Recurso não encontrado | 404 |
| Email duplicado no registro | 409 |
| Erro de validação | 400 |

---

## Decisões de Arquitetura

**Por que jjwt em vez do Spring OAuth2 Resource Server?**
O controle manual do JWT com jjwt oferece visibilidade total sobre geração, validação e renovação de tokens — o que demonstra melhor o entendimento do mecanismo para um projeto de portfólio.

**Por que MapStruct em vez de mapeamento manual?**
Elimina boilerplate mantendo segurança em tempo de compilação. O código gerado é visível e inspecionável, ao contrário de mapeadores baseados em reflection.

**Por que JpaSpecificationExecutor para filtros de tarefas?**
Permite combinar filtros dinâmicos (status, prioridade, tag) sem proliferação de métodos no repositório. A especificação `hasTag` trata o join com `@ElementCollection` com `distinct` correto para evitar duplicatas nas queries de contagem da paginação.

**Por que H2 nos testes em vez de Testcontainers?**
Setup mais simples sem dependência do Docker. O H2 em `MODE=PostgreSQL` cobre todos os cenários testados. Testcontainers seria o próximo passo para maior paridade com produção.

**Estratégia de cascade delete**
O cascade é tratado no banco via `ON DELETE CASCADE` nas migrations do Flyway. Isso evita carregar entidades filhas em memória e é mais eficiente do que o cascade JPA para deleções em massa.
