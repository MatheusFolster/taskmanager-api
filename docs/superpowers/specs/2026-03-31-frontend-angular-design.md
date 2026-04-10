# Frontend Angular — Design Spec

**Data:** 2026-03-31
**Status:** Aprovado

---

## Objetivo

Interface web que consome a Task Manager API, demonstrando capacidade fullstack. Foco em interface bonita e funcional — não em cobertura completa de features. Sem landing page; apenas o app autenticado.

---

## Stack

| Camada | Tecnologia |
|---|---|
| Framework | Angular 19+ (standalone components) |
| Estilos | Tailwind CSS |
| Componentes UI | PrimeNG |
| HTTP | Angular HttpClient |
| Estado | Serviços simples (sem NgRx) |

---

## Estrutura de Pastas

```
teste-manager-frontend/
├── src/app/
│   ├── core/
│   │   ├── services/          # AuthService, ProjectService, TaskService, SubtaskService
│   │   ├── interceptors/      # auth.interceptor.ts
│   │   ├── guards/            # auth.guard.ts
│   │   └── models/            # interfaces TypeScript
│   ├── features/
│   │   ├── auth/              # LoginComponent, RegisterComponent
│   │   ├── projects/          # ProjectsListComponent, ProjectFormComponent
│   │   ├── tasks/             # TasksListComponent, TaskFormComponent
│   │   └── subtasks/          # embutido em TaskDetailComponent
│   ├── shared/
│   │   └── components/        # ConfirmDialogComponent e outros reutilizáveis
│   ├── app.routes.ts
│   └── app.config.ts
```

---

## Rotas

| Rota | Componente | Acesso |
|---|---|---|
| `/login` | LoginComponent | Pública |
| `/register` | RegisterComponent | Pública |
| `/projects` | ProjectsListComponent | Protegida |
| `/projects/:id/tasks` | TasksListComponent | Protegida |
| `/tasks/:id` | TaskDetailComponent | Protegida |
| `**` | redirect | → `/projects` |

- Rotas protegidas via `AuthGuard`
- Login bem-sucedido → `/projects`
- Logout → `/login`

---

## Telas

### Login / Register
- Formulário centralizado na tela
- Campos: email, senha
- Link para alternar entre login e registro
- Toast de erro em credenciais inválidas

### Lista de Projetos (`/projects`)
- Navbar com nome do usuário e botão logout
- Grid de cards — um por projeto
- Cada card: nome, descrição, badge de status (ACTIVE/ARCHIVED), ações (arquivar, deletar)
- Botão "Novo Projeto" abre `p-dialog` com formulário inline

### Lista de Tarefas (`/projects/:id/tasks`)
- Header com nome do projeto e botão voltar
- Filtros por status e prioridade
- `p-datatable` com colunas: título, status, prioridade, data de vencimento, tags, progresso
- Botão "Nova Tarefa" abre `p-dialog`
- Clique na linha navega para `/tasks/:id`

### Detalhe da Tarefa (`/tasks/:id`)
- Header com título, status e prioridade da tarefa
- `p-progressbar` com percentual de subtarefas concluídas
- Lista de subtarefas: checkbox para concluir, botão deletar
- Campo inline para adicionar nova subtarefa

---

## Serviços

### AuthService
- `login(email, password)` → salva tokens no localStorage
- `register(name, email, password)` → salva tokens no localStorage
- `logout()` → limpa localStorage, redireciona para `/login`
- `isLoggedIn()` → verifica presença de token

### AuthInterceptor
- Injeta `Authorization: Bearer <token>` em toda requisição
- Em resposta 401: tenta refresh uma vez; se falhar, redireciona para `/login`

### ProjectService
`list()`, `create()`, `update()`, `archive()`, `delete()`

### TaskService
`listByProject()`, `getById()`, `create()`, `updateStatus()`, `delete()`

### SubtaskService
`listByTask()`, `create()`, `complete()`, `delete()`

---

## Design Visual

Seguir `docs/DESIGN-GUIDELINES.md`:
- Paleta monocromática (referência Vercel/Linear)
- Fonte Inter (sans) + JetBrains Mono (código)
- Cards com border sutil, hover com shadow leve
- Badges de status e prioridade com cores semânticas
- PrimeNG customizado via `styleClass` Tailwind — nunca `style` inline