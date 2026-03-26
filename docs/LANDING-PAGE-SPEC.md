# Task Manager API — Landing Page Spec

**Data:** 2026-03-26
**Versão:** 1.0

> Especificação estrutural da landing page. Sem copy — apenas estrutura, objetivos e diretrizes de layout.

---

## Princípios

- Um objetivo: levar o visitante ao repositório GitHub
- Clareza total em 5 segundos
- Estilo: developer tool (referência: Vercel, Resend)
- Desktop-first (audiência é dev)

---

## Estrutura de Seções

```
1. Header/Nav
2. Hero
3. Features
4. Architecture
5. Tech Stack
6. How It Works
7. CTA Final
8. Footer
```

---

## Seções Detalhadas

---

### 1. HEADER / NAV

**Objetivo:** Navegação mínima, sem distrair do CTA principal.

**Elementos:**
- Logo / nome do projeto (esquerda)
- Links: Features · Arquitetura · Stack (centro)
- CTA: "Ver no GitHub" com ícone GitHub (direita, botão outlined)

**Diretrizes:**
- Sticky no scroll
- Background transparente no hero, sólido ao scrollar (blur + border-bottom sutil)
- Sem mega menu
- Mobile: links colapsam em menu hambúrguer

---

### 2. HERO

**Objetivo:** Comunicar o valor técnico do projeto em 5 segundos.

**Elementos obrigatórios:**
- Badge superior (ex: "Projeto de Portfólio · Spring Boot")
- Headline principal (H1) — grande, bold
- Subheadline — 1-2 linhas em cor muted
- CTA primário: "Ver no GitHub"
- CTA secundário: "Ver Documentação (Swagger)"
- Visual: bloco de código ou terminal mostrando a estrutura de pacotes ou um endpoint de exemplo

**Diretrizes:**
- Visual à direita, texto à esquerda (layout 50/50)
- Código com syntax highlight (tema dark mesmo em light mode)
- Gradiente sutil ou grid pattern no background
- Animação sutil: cursor piscando no terminal ou fade-in do código

**Layout:**
```
[Badge]
[H1 grande e bold          ] [ Bloco de código           ]
[Subheadline muted         ] [ com syntax highlight      ]
[CTA Primary] [CTA Secondary]
```

---

### 3. FEATURES

**Objetivo:** Mostrar os diferenciais técnicos do projeto de forma visual.

**Elementos:**
- Título da seção
- 6 cards, grid 3x2
- Cada card: ícone + título + descrição curta

**Cards sugeridos:**
1. Arquitetura em Camadas
2. Autenticação JWT
3. Domínio Rico (Projetos → Tarefas → Subtarefas)
4. Cobertura de Testes ≥ 80%
5. Tratamento de Erros Centralizado
6. Documentação Swagger/OpenAPI

**Diretrizes:**
- Cards com border sutil, hover com elevação leve (shadow)
- Ícones outline, monocromáticos
- Descrição máximo 2 linhas
- Grid responsivo: 3 colunas desktop → 2 tablet → 1 mobile

**Layout:**
```
[ ícone + título + desc ] [ ícone + título + desc ] [ ícone + título + desc ]
[ ícone + título + desc ] [ ícone + título + desc ] [ ícone + título + desc ]
```

---

### 4. ARCHITECTURE

**Objetivo:** Demonstrar visualmente a organização do código — principal diferencial para avaliadores técnicos.

**Elementos:**
- Título da seção
- Diagrama de fluxo: Controller → Service → Repository → DB
- Ao lado: árvore de pastas estilizada (estilo VS Code file tree)
- Legenda explicando cada camada

**Diretrizes:**
- Diagrama em SVG ou componente visual, não imagem
- File tree com ícones de arquivo
- Background levemente diferente da seção anterior (separação visual)
- Layout: diagrama à esquerda, file tree à direita

**Layout:**
```
[Título centralizado]

[ Diagrama de fluxo      ] [ File tree estilizada    ]
[ Controller → Service   ] [ com.taskmanager/        ]
[ → Repository → DB      ] [   ├── controller/       ]
                           [   ├── service/           ]
                           [   └── ...                ]
```

---

### 5. TECH STACK

**Objetivo:** Comunicar rapidamente as tecnologias usadas.

**Elementos:**
- Título da seção
- Grid de badges/pills de tecnologia
- Agrupados por categoria: Backend · Testes · Documentação · Frontend

**Diretrizes:**
- Badges com ícone + nome da tecnologia
- Cores neutras (não usar cores oficiais das marcas)
- Layout horizontal com wrap
- Seção com background diferente (cinza claro / surface)

**Layout:**
```
[Título]

Backend:     [ Java 21 ] [ Spring Boot ] [ Spring Security ] [ JPA ]
Banco:       [ PostgreSQL ] [ H2 ]
Testes:      [ JUnit 5 ] [ Mockito ] [ MockMvc ]
Docs:        [ Swagger ] [ OpenAPI 3 ]
Frontend:    [ Angular ] [ Tailwind ] [ PrimeNG ]
```

---

### 6. HOW IT WORKS

**Objetivo:** Mostrar o fluxo de uso da API de forma simples.

**Elementos:**
- Título da seção
- 4 steps numerados em linha
- Cada step: número + título + descrição curta
- Visual: snippet de request/response para o step mais importante (step 2 ou 3)

**Steps sugeridos:**
1. Registrar / Autenticar — obter JWT
2. Criar um Projeto — agrupar tarefas
3. Adicionar Tarefas — com prioridade, status e tags
4. Gerenciar Subtarefas — acompanhar progresso

**Diretrizes:**
- Steps em linha horizontal com connector entre eles
- Número em destaque (grande, cor primary)
- Snippet de código abaixo dos steps (request HTTP de exemplo)
- Mobile: steps empilhados verticalmente

**Layout:**
```
[1]——————[2]——————[3]——————[4]
Auth   Projeto  Tarefa  Subtarefa

[ Exemplo de request/response em bloco de código ]
```

---

### 7. CTA FINAL

**Objetivo:** Última conversão — levar ao GitHub ou Swagger.

**Elementos:**
- Headline de fechamento
- Subheadline curta
- CTA primário: "Ver código no GitHub"
- CTA secundário: "Explorar Swagger"

**Diretrizes:**
- Background diferenciado: cor sólida escura ou gradiente sutil
- Centralizado
- Sem mais informações — só o CTA
- Ícone GitHub no botão primário

**Layout:**
```
[          Headline centralizada           ]
[          Subheadline muted              ]
[ Btn: GitHub ]  [ Btn: Swagger (outlined) ]
```

---

### 8. FOOTER

**Objetivo:** Links úteis e créditos.

**Elementos:**
- Nome do projeto + descrição de uma linha
- Links: GitHub · Swagger · README
- Linha de copyright

**Diretrizes:**
- Simples, sem elaboração
- Uma linha ou duas colunas no máximo
- Border-top sutil separando do conteúdo

---

## Hierarquia de CTAs

| Prioridade | CTA | Localização |
|---|---|---|
| P1 | Ver no GitHub | Nav + Hero + CTA Final |
| P2 | Explorar Swagger | Hero (secundário) + CTA Final |
| P3 | Scroll para features | Hero (anchor link) |

---

## Checklist Pré-Launch

- [ ] Hero comunica o valor em 5 segundos?
- [ ] Bloco de código renderiza com syntax highlight?
- [ ] Links do GitHub e Swagger funcionando?
- [ ] Responsivo em mobile (768px e 375px)?
- [ ] Seções bem separadas visualmente?
- [ ] CTA acima da dobra?
