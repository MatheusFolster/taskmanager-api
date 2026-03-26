# Task Manager API — MVP Scope

**Data:** 2026-03-26
**Versão:** 1.0

---

## Visão do MVP

**Em uma frase:**
> API REST de gestão de tarefas com hierarquia Projetos → Tarefas → Subtarefas, autenticação JWT e cobertura de testes, demonstrando arquitetura Spring Boot profissional.

**Hipótese principal:**
> Um projeto de portfólio com domínio rico, arquitetura em camadas bem definida e testes cobrindo os fluxos críticos demonstra maturidade técnica de forma convincente para avaliadores técnicos.

**Como saberemos que funcionou:**
> Cobertura ≥ 80%, zero erros de lint, Swagger completo, README com decisões de arquitetura documentadas.

---

## O que ENTRA no MVP

### Must Have — P0 (sem isso não sobe)

| Feature | Descrição | Critério de Done |
|---|---|---|
| Auth JWT | Register + Login + proteção de rotas | Testes unitários + integração passando |
| CRUD Projetos | Criar, listar, atualizar, arquivar, deletar | Cascade delete funcionando |
| CRUD Tarefas | Com status, prioridade, tags, dueDate | Filtros e paginação funcionando |
| CRUD Subtarefas | Criar, concluir, deletar | Progresso da tarefa calculado |
| Testes — Service | Cobertura ≥ 80% na camada service | JUnit 5 + Mockito |
| Testes — Controller | Testes de integração nos endpoints principais | MockMvc + H2 |
| Tratamento de Erros | GlobalExceptionHandler com respostas padronizadas | Todos os edge cases mapeados |
| Swagger | Documentação completa de todos os endpoints | Acessível em /swagger-ui.html |

### Should Have — P1 (importante, entra se der tempo)

| Feature | Descrição | Por que não é P0 |
|---|---|---|
| Refresh Token | Renovação de JWT sem novo login | Auth funciona sem, é melhoria de UX |
| Ordenação de tarefas | Por dueDate, prioridade, createdAt | Filtros básicos já demonstram o conceito |
| README completo | Diagrama de arquitetura, decisões técnicas, setup | Pode ser escrito no fim |

### Could Have — P2 (nice to have)

| Feature | Descrição | Quando considerar |
|---|---|---|
| Frontend Angular | Tela básica consumindo a API | Após API 100% pronta e testada |
| Docker Compose | API + PostgreSQL em container | Se sobrar tempo no mês |
| GitHub Actions CI | Pipeline rodando testes automaticamente | Agrega muito ao portfólio |

---

## O que NÃO ENTRA no MVP

| Feature | Por que não entra | Quando reconsiderar |
|---|---|---|
| Colaboração entre usuários | Aumenta complexidade sem agregar ao foco (arquitetura) | v2 se virar produto real |
| Times e permissões por role | Mesmo motivo — desvio do foco | v2 |
| Notificações (email/push) | Fora do escopo técnico pretendido | Nunca nesse projeto |
| Real-time (WebSocket) | Aumenta complexidade sem demonstrar o objetivo | Projeto separado |
| Multi-tenancy | Desnecessário para portfólio solo | v2 |
| App mobile | Fora da stack definida | Projeto separado |
| Relatórios / dashboards | Sem valor para o objetivo de portfólio | v2 |
| Import/Export de tarefas | Baixo valor técnico para demonstrar | Nunca nesse projeto |

---

## Decisões de Simplificação

### Autenticação
- ✅ Email + senha com JWT
- ✅ Refresh token (P1)
- ❌ OAuth (Google, GitHub) — fora do escopo

### Banco de Dados
- ✅ PostgreSQL em produção
- ✅ H2 em memória para testes
- ❌ Redis / cache — desnecessário no MVP

### Testes
- ✅ JUnit 5 + Mockito para unitários
- ✅ MockMvc para integração de controllers
- ✅ TestContainers (opcional, se quiser demonstrar)
- ❌ Testes E2E — fora do escopo

### UI
- ✅ Swagger UI como interface de documentação
- ✅ Frontend Angular como P2 (só após API pronta)
- ❌ Design elaborado — não é o foco

---

## Timeline Estimado (1 mês)

| Fase | Duração | Entregáveis |
|---|---|---|
| Setup + Auth | 5 dias | Projeto base, JWT funcionando, testes de auth |
| Domínio (Projetos + Tarefas) | 8 dias | CRUD completo com filtros e testes |
| Subtarefas + Edge Cases | 4 dias | Hierarquia completa, cascade, progresso |
| Testes + Qualidade | 5 dias | Cobertura ≥ 80%, refactor, lint |
| Swagger + README | 3 dias | Documentação completa |
| Buffer / P1s | 5 dias | Refresh token, ordenação, ajustes |
| **Total** | **30 dias** | |

---

## Definition of Done

O MVP está pronto quando:

- [ ] Todos os endpoints P0 funcionando e documentados no Swagger
- [ ] Cobertura de testes ≥ 80% nas camadas Service e Controller
- [ ] GlobalExceptionHandler cobrindo todos os edge cases mapeados
- [ ] Nenhum dado sensível hardcoded no código
- [ ] README com: setup local, decisões de arquitetura, diagrama de entidades
- [ ] Código passando sem warnings no SonarLint/Checkstyle

---

## Hipóteses a Validar

| Hipótese | Como validar | Sucesso = |
|---|---|---|
| Domínio rico demonstra maturidade técnica | Feedback de devs sêniores | Aprovação positiva |
| Testes bem escritos impressionam avaliadores | Review do código por recrutadores técnicos | Chamada para entrevista |
| Arquitetura em camadas é legível sem explicação | Navegação no repo sem README | Estrutura auto-explicativa |

---

## Regra de Ouro

> "Posso demonstrar arquitetura profissional Spring Boot SEM essa feature?"
>
> Se sim → Não entra no MVP.
