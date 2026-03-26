# Task Manager API — Design Guidelines

**Data:** 2026-03-26
**Versão:** 1.0

> Diretrizes visuais para a landing page e frontend Angular.
> Estilo: Developer tool. Referências: Vercel, Resend, Linear.

---

## Estilo Visual

- **Modo:** Light mode (dark apenas em blocos de código)
- **Personalidade:** Técnico, limpo, preciso, profissional
- **Não é:** Colorido, lúdico, rounded demais, cheio de gradientes
- **Referências:** vercel.com · resend.com · linear.app

---

## Paleta de Cores

### Cores Principais

| Token | Hex | Uso |
|---|---|---|
| `--color-background` | `#FFFFFF` | Background principal |
| `--color-surface` | `#F9FAFB` | Cards, seções alternadas |
| `--color-border` | `#E5E7EB` | Bordas, divisores |
| `--color-text-primary` | `#111827` | Títulos, textos principais |
| `--color-text-secondary` | `#6B7280` | Subtítulos, descrições, labels |
| `--color-text-muted` | `#9CA3AF` | Placeholder, texto desabilitado |

### Cor de Acento (Primary)

| Token | Hex | Uso |
|---|---|---|
| `--color-primary` | `#18181B` | CTAs primários, links ativos |
| `--color-primary-hover` | `#27272A` | Hover de CTAs primários |
| `--color-primary-foreground` | `#FFFFFF` | Texto sobre fundo primary |

> Acento escuro (quase preto) é o padrão Vercel/Resend — transmite seriedade técnica.

### Cores Semânticas

| Token | Hex | Uso |
|---|---|---|
| `--color-success` | `#16A34A` | Status DONE, testes passando |
| `--color-warning` | `#D97706` | Prioridade HIGH, alertas |
| `--color-danger` | `#DC2626` | Erros, prioridade URGENT |
| `--color-info` | `#2563EB` | Links, badges informativos |

### Status de Tarefas

| Status | Cor | Hex |
|---|---|---|
| TODO | Cinza | `#6B7280` |
| IN_PROGRESS | Azul | `#2563EB` |
| DONE | Verde | `#16A34A` |
| CANCELLED | Vermelho apagado | `#9CA3AF` com strike |

### Prioridades

| Prioridade | Cor | Hex |
|---|---|---|
| LOW | Cinza claro | `#D1D5DB` |
| MEDIUM | Azul | `#93C5FD` |
| HIGH | Laranja | `#FCD34D` |
| URGENT | Vermelho | `#FCA5A5` |

---

## Tipografia

### Font Family

```css
--font-sans: 'Inter', system-ui, -apple-system, sans-serif;
--font-mono: 'JetBrains Mono', 'Fira Code', 'Cascadia Code', monospace;
```

- **Inter** — para todo texto de interface (Google Fonts)
- **JetBrains Mono** — exclusivo para blocos de código, snippets, file trees

### Escala Tipográfica

| Token | Size | Weight | Line Height | Uso |
|---|---|---|---|---|
| `--text-xs` | 12px | 400 | 1.5 | Labels, badges |
| `--text-sm` | 14px | 400 | 1.5 | Corpo de texto, descrições |
| `--text-base` | 16px | 400 | 1.6 | Texto principal |
| `--text-lg` | 18px | 500 | 1.5 | Subtítulos de cards |
| `--text-xl` | 20px | 600 | 1.4 | Títulos de seção (h3) |
| `--text-2xl` | 24px | 600 | 1.3 | Títulos de seção (h2) |
| `--text-4xl` | 36px | 700 | 1.2 | Headline hero (mobile) |
| `--text-6xl` | 60px | 700 | 1.1 | Headline hero (desktop) |

---

## Espaçamento

Base: **8px grid**

| Token | Valor | Uso |
|---|---|---|
| `--space-1` | 4px | Gap mínimo entre elementos inline |
| `--space-2` | 8px | Padding interno de badges/chips |
| `--space-3` | 12px | Gap entre ícone e label |
| `--space-4` | 16px | Padding interno de cards pequenos |
| `--space-6` | 24px | Gap entre cards |
| `--space-8` | 32px | Padding interno de cards grandes |
| `--space-12` | 48px | Espaço entre subseções |
| `--space-16` | 64px | Padding de seções (mobile) |
| `--space-24` | 96px | Padding de seções (desktop) |

---

## Border Radius

| Token | Valor | Uso |
|---|---|---|
| `--radius-sm` | 4px | Badges, chips, inputs |
| `--radius-md` | 8px | Cards, botões |
| `--radius-lg` | 12px | Modais, panels |
| `--radius-xl` | 16px | Cards de destaque |
| `--radius-full` | 9999px | Pills, avatares |

---

## Sombras

Sombras sutis — não usar sombras pesadas (estilo Material).

| Token | Valor | Uso |
|---|---|---|
| `--shadow-sm` | `0 1px 2px rgba(0,0,0,0.05)` | Cards em repouso |
| `--shadow-md` | `0 4px 6px rgba(0,0,0,0.07)` | Cards em hover |
| `--shadow-lg` | `0 10px 15px rgba(0,0,0,0.08)` | Modais, dropdowns |
| `--shadow-none` | `none` | Elementos flat |

---

## Componentes (PrimeNG + Tailwind)

### Botões

| Tipo | Estilo | Quando usar |
|---|---|---|
| Primary | Background `--color-primary`, texto branco, `radius-md` | CTA principal, ação destrutiva confirmada |
| Secondary | Border `--color-border`, texto `--color-text-primary`, bg transparente | CTA secundário, ação neutra |
| Ghost | Sem border, sem background, texto `--color-text-secondary` | Ações em tabelas, ações menos importantes |
| Danger | Background `#DC2626`, texto branco | Deletar, cancelar permanente |

### Cards

```
border: 1px solid var(--color-border)
border-radius: var(--radius-lg)
background: var(--color-background)
padding: var(--space-6)
box-shadow: var(--shadow-sm)
transition: box-shadow 150ms ease

hover:
  box-shadow: var(--shadow-md)
```

### Inputs

```
border: 1px solid var(--color-border)
border-radius: var(--radius-sm)
padding: 8px 12px
font-size: var(--text-sm)
background: var(--color-background)

focus:
  border-color: var(--color-primary)
  outline: 2px solid rgba(24,24,27,0.1)
```

### Badges / Status Pills

```
font-size: var(--text-xs)
font-weight: 500
padding: 2px 8px
border-radius: var(--radius-full)
```

### Blocos de Código

```
font-family: var(--font-mono)
font-size: 13px
background: #0A0A0A  (dark mesmo em light mode)
color: #E5E7EB
border-radius: var(--radius-md)
padding: var(--space-6)
overflow-x: auto
```

---

## Diretrizes de Uso do PrimeNG

| Componente PrimeNG | Usar para |
|---|---|
| `p-button` | Todos os botões — customizar via `styleClass` com Tailwind |
| `p-inputtext` | Inputs de texto e busca |
| `p-datatable` | Listagem de tarefas e projetos |
| `p-tag` | Status e prioridade das tarefas |
| `p-badge` | Contadores (ex: nº de subtarefas) |
| `p-dialog` | Modais de criação/edição |
| `p-toast` | Feedback de ações (sucesso, erro) |
| `p-skeleton` | Loading states |
| `p-progressbar` | Progresso de subtarefas |
| `p-menu` | Dropdown de ações em cards |

> Sobrescrever estilos PrimeNG via `styleClass` do Tailwind — nunca via `style` inline.

---

## Animações e Transições

- **Princípio:** Micro-interações sutis, nunca chamar atenção para si mesmas
- **Duração padrão:** 150ms (hover), 200ms (enter), 150ms (exit)
- **Easing:** `ease` para hover, `ease-out` para entrar, `ease-in` para sair

| Situação | Transição |
|---|---|
| Hover em card | `box-shadow` 150ms ease |
| Hover em botão | `background-color` 150ms ease |
| Abrir modal | fade + scale de 0.95 → 1, 200ms ease-out |
| Toast notification | slide-in da direita, 200ms ease-out |
| Loading skeleton | pulse animation, 1.5s infinite |

---

## Responsividade

| Breakpoint | Min-width | Comportamento |
|---|---|---|
| Mobile | 0px | 1 coluna, nav colapsada |
| Tablet | 768px | 2 colunas, nav expandida |
| Desktop | 1024px | Layout completo |
| Wide | 1280px | Max-width container = 1200px |

---

## Referências Visuais

| Site | O que estudar |
|---|---|
| vercel.com | Hero com terminal, paleta monocromática |
| resend.com | Tipografia bold, blocos de código, CTA |
| linear.app | Cards, animações sutis, espaçamento |
| supabase.com | Stack section, developer-focused layout |
