# Frontend Landing Page — Design Spec

**Data:** 2026-04-08
**Versão:** 1.0
**Status:** Aprovado

---

## Contexto

Landing page estática para o Task Manager API, construída em Angular 21 com Tailwind CSS. Objetivo único: levar o visitante ao repositório GitHub. Audiência: avaliadores técnicos e recrutadores.

Referências de conteúdo: `docs/LANDING-PAGE-SPEC.md` e `docs/DESIGN-GUIDELINES.md`.

---

## Escopo

- Landing page com 8 seções (conforme LANDING-PAGE-SPEC.md)
- Sem app de tarefas (login, CRUD) — isso é fase posterior
- Sem roteamento entre páginas — âncoras para navegação interna
- Conteúdo 100% estático — nenhum HTTP call à API

---

## Reorganização de Pastas

O projeto Angular está atualmente aninhado em `teste-manager-frontend/teste-manager-frontend/`. O conteúdo sobe um nível para `teste-manager-frontend/`, incorporando o `tailwind.config.js` existente na pasta externa.

Estrutura final:

```
teste-manager-frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── header/
│   │   │   │   ├── header.component.ts
│   │   │   │   └── header.component.html
│   │   │   ├── hero/
│   │   │   │   ├── hero.component.ts
│   │   │   │   └── hero.component.html
│   │   │   ├── features/
│   │   │   │   ├── features.component.ts
│   │   │   │   └── features.component.html
│   │   │   ├── architecture/
│   │   │   │   ├── architecture.component.ts
│   │   │   │   └── architecture.component.html
│   │   │   ├── tech-stack/
│   │   │   │   ├── tech-stack.component.ts
│   │   │   │   └── tech-stack.component.html
│   │   │   ├── how-it-works/
│   │   │   │   ├── how-it-works.component.ts
│   │   │   │   └── how-it-works.component.html
│   │   │   ├── cta-final/
│   │   │   │   ├── cta-final.component.ts
│   │   │   │   └── cta-final.component.html
│   │   │   └── footer/
│   │   │       ├── footer.component.ts
│   │   │       └── footer.component.html
│   │   ├── app.component.ts
│   │   ├── app.component.html
│   │   ├── app.routes.ts
│   │   └── app.config.ts
│   ├── styles.css
│   └── index.html
├── angular.json
├── tailwind.config.js
├── postcss.config.js
├── package.json
└── tsconfig.json
```

---

## Dependências

| Pacote | Versão | Uso |
|---|---|---|
| `tailwindcss` | v3 | Layout, espaçamento, responsividade |
| `postcss` | latest | Processamento do Tailwind |
| `autoprefixer` | latest | Compatibilidade de prefixos CSS |
| `highlight.js` | latest | Syntax highlighting nos blocos de código |

Sem PrimeNG — overkill para landing page estática.

---

## Arquitetura de Componentes

Todos os componentes são **Angular standalone**. Nenhum NgModule. Sem serviços, sem estado reativo — conteúdo 100% estático declarado diretamente nos templates.

### AppComponent
Orquestra os 8 componentes em sequência. Sem lógica própria.

```html
<app-header />
<main>
  <app-hero />
  <app-features />
  <app-architecture />
  <app-tech-stack />
  <app-how-it-works />
  <app-cta-final />
</main>
<app-footer />
```

### HeaderComponent
- Nav sticky com `position: sticky; top: 0`
- Background: `bg-transparent` → `bg-white/90 backdrop-blur-sm border-b border-gray-200` ao scrollar
- Scroll behavior via `@HostListener('window:scroll')`
- Links de âncora: Features · Arquitetura · Stack
- CTA: "Ver no GitHub" (botão outlined, ícone GitHub SVG)
- Mobile: links colapsam em menu hambúrguer (toggle com `[class.open]`)

### HeroComponent
- Layout 50/50: texto à esquerda, código à direita
- Badge superior, H1 grande (60px desktop / 36px mobile), subheadline muted
- CTA primário: "Ver no GitHub" | CTA secundário: "Ver Swagger"
- Bloco de código com `highlight.js` (tema `github-dark`), mostrando exemplo de endpoint
- Background: grid pattern sutil via CSS (`background-image: linear-gradient`)

### FeaturesComponent
- Grid 3×2 de cards (`grid-cols-3 md:grid-cols-2 sm:grid-cols-1`)
- Cada card: ícone SVG outline + título + descrição (máx 2 linhas)
- Hover: `shadow-sm → shadow-md` em 150ms
- 6 cards: Arquitetura em Camadas, Auth JWT, Domínio Rico, Cobertura ≥ 80%, Erros Centralizados, Swagger

### ArchitectureComponent
- Background `bg-gray-50` (separação visual)
- Layout: diagrama SVG à esquerda, file tree à direita
- Diagrama: Controller → Service → Repository → Database (setas com SVG)
- File tree: fonte JetBrains Mono, ícones de pasta/arquivo inline SVG

### TechStackComponent
- Badges agrupados por categoria em layout horizontal com wrap
- Categorias: Backend · Banco · Testes · Docs · Frontend
- Cada badge: ícone SVG + nome, fundo `bg-gray-100`, texto `text-gray-700`, border `border-gray-200`

### HowItWorksComponent
- 4 steps numerados em linha horizontal com conector entre eles
- Número grande em destaque (cor primary)
- Snippet de código abaixo: exemplo de request/response do endpoint de criação de tarefa
- Mobile: steps empilhados verticalmente

### CtaFinalComponent
- Background escuro: `bg-gray-900` texto branco
- Headline centralizada + subheadline muted
- CTA primário: "Ver código no GitHub" (botão branco)
- CTA secundário: "Explorar Swagger" (botão outlined branco)

### FooterComponent
- Uma linha: nome do projeto + links (GitHub · Swagger · README)
- `border-t border-gray-200`, texto `text-gray-500`
- Copyright

---

## Estilos Globais (`styles.css`)

```css
@tailwind base;
@tailwind components;
@tailwind utilities;

:root {
  --color-primary: #18181B;
  --color-primary-hover: #27272A;
  --color-surface: #F9FAFB;
  --color-border: #E5E7EB;
  --color-text-primary: #111827;
  --color-text-secondary: #6B7280;
  --font-sans: 'Inter', system-ui, sans-serif;
  --font-mono: 'JetBrains Mono', monospace;
}

body {
  font-family: var(--font-sans);
  color: var(--color-text-primary);
  background: #ffffff;
}
```

---

## `index.html` — Google Fonts

```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
```

---

## `tailwind.config.js`

```js
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      colors: {
        primary: '#18181B',
      },
    },
  },
  plugins: [],
};
```

---

## URLs

| Destino | URL |
|---|---|
| GitHub | `https://github.com/MatheusFolster/taskmanager-api` |
| Swagger | `http://localhost:8080/swagger-ui.html` *(placeholder — trocar ao fazer deploy)* |

---

## Responsividade

| Breakpoint | Comportamento |
|---|---|
| Mobile (< 768px) | 1 coluna, nav hambúrguer, hero empilhado, steps verticais |
| Tablet (768px+) | 2 colunas nos features, nav expandida |
| Desktop (1024px+) | Layout completo, max-width 1200px centralizado |

---

## O que NÃO está no escopo

- Nenhuma chamada HTTP à API
- Nenhuma tela de login ou app de tarefas
- Nenhum PrimeNG
- Nenhum Angular SSR / pre-rendering
- Nenhum i18n
