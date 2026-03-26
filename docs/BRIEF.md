# Task Manager API

**Data:** 2026-03-26
**Autor:** —
**Status:** Building

---

## 💡 Problema

Developers em nível intermediário precisam de um projeto de portfólio que demonstre domínio real de arquitetura backend — não apenas um CRUD básico, mas um sistema com domínio rico, padrões de projeto e qualidade de código comprovada por testes.

**Contexto:**
A maioria dos projetos de portfólio com Spring Boot são superficiais: um CRUD sem camadas, sem testes, sem modelagem de domínio. Isso não demonstra maturidade técnica para recrutadores e tech leads que avaliam código.

---

## ✅ Solução

Uma API REST de gestão de tarefas com domínio rico (Projetos → Tarefas → Subtarefas), autenticação JWT, cobertura de testes e arquitetura em camadas seguindo Clean Code, SOLID e DDD.

**Como funciona:**
O usuário autentica via JWT e gerencia seus projetos, tarefas e subtarefas através de endpoints REST bem definidos. O código é organizado em camadas (Controller → Service → Repository), com DTOs, Mappers, tratamento de exceções e testes unitários + de integração cobrindo os fluxos críticos.

---

## 👤 Público-Alvo

**Persona principal:**
Desenvolvedor backend/fullstack intermediário que quer demonstrar maturidade técnica com Spring Boot para o mercado de trabalho.

**Audiência do portfólio:**
Tech leads e recrutadores técnicos que avaliam projetos no GitHub antes de entrevistas.

---

## 🎯 Proposta de Valor

> Projeto de portfólio que demonstra arquitetura profissional Spring Boot — não o que você sabe fazer, mas como você pensa e organiza código.

**Alternativas comuns em portfólios:**
- CRUD simples sem camadas
- Projeto sem testes
- Código sem tratamento de erros

**Diferencial deste projeto:**
- Domínio rico com 3 níveis de hierarquia
- Arquitetura em camadas com separação clara de responsabilidades
- Testes unitários e de integração
- JWT implementado corretamente
- Tratamento de exceções centralizado

---

## 💰 Modelo de Negócio

Não se aplica — projeto de portfólio open source.

---

## 📊 Métricas de Sucesso

**North Star:** Projeto demonstra domínio técnico convincente para recrutadores

**Metas:**
- [ ] Cobertura de testes ≥ 80%
- [ ] Todos os endpoints documentados via Swagger
- [ ] Zero warnings de código (Checkstyle/SonarLint)
- [ ] README completo com instruções de setup e decisões de arquitetura

---

## 🛠 Stack

| Camada | Tecnologia |
|--------|------------|
| Backend | Java 21 + Spring Boot 3.x |
| Segurança | Spring Security + JWT (jjwt) |
| Persistência | Spring Data JPA + PostgreSQL |
| Testes | JUnit 5 + Mockito + H2 |
| Documentação | Swagger / OpenAPI 3 |
| Frontend | Angular (Latest) + Tailwind + PrimeNG |

---

## ⏱ Timeline

| Marco | Prazo |
|-------|-------|
| Setup + Auth | Semana 1 |
| Domínio completo (Projetos, Tarefas, Subtarefas) | Semana 2-3 |
| Testes + Qualidade | Semana 3-4 |
| Swagger + README | Semana 4 |
