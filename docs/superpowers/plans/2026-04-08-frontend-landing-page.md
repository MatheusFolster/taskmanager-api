# Frontend Landing Page — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a static Angular 21 landing page for Task Manager API with 8 sections (Header, Hero, Features, Architecture, TechStack, HowItWorks, CtaFinal, Footer) using Tailwind CSS.

**Architecture:** 8 standalone Angular components orchestrated by AppComponent. No HTTP calls, no state management — pure static content. Tailwind CSS for layout/styling, highlight.js for code syntax highlighting in Hero and HowItWorks.

**Tech Stack:** Angular 21, TypeScript 5.9, Tailwind CSS 3.4, highlight.js, Vitest (via `@angular/build:unit-test`)

---

## File Map

Files to be **created**:
```
teste-manager-frontend/
├── postcss.config.js
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── header/
│   │   │   │   ├── header.ts
│   │   │   │   ├── header.html
│   │   │   │   └── header.spec.ts
│   │   │   ├── hero/
│   │   │   │   ├── hero.ts
│   │   │   │   ├── hero.html
│   │   │   │   └── hero.spec.ts
│   │   │   ├── features/
│   │   │   │   ├── features.ts
│   │   │   │   ├── features.html
│   │   │   │   └── features.spec.ts
│   │   │   ├── architecture/
│   │   │   │   ├── architecture.ts
│   │   │   │   ├── architecture.html
│   │   │   │   └── architecture.spec.ts
│   │   │   ├── tech-stack/
│   │   │   │   ├── tech-stack.ts
│   │   │   │   ├── tech-stack.html
│   │   │   │   └── tech-stack.spec.ts
│   │   │   ├── how-it-works/
│   │   │   │   ├── how-it-works.ts
│   │   │   │   ├── how-it-works.html
│   │   │   │   └── how-it-works.spec.ts
│   │   │   ├── cta-final/
│   │   │   │   ├── cta-final.ts
│   │   │   │   ├── cta-final.html
│   │   │   │   └── cta-final.spec.ts
│   │   │   └── footer/
│   │   │       ├── footer.ts
│   │   │       ├── footer.html
│   │   │       └── footer.spec.ts
```

Files to be **modified**:
```
teste-manager-frontend/
├── tailwind.config.js          ← update content paths
├── package.json                ← add tailwind + highlight.js deps
├── src/
│   ├── index.html              ← add Google Fonts
│   ├── styles.css              ← add Tailwind directives + CSS tokens
│   └── app/
│       ├── app.ts              ← replace with orchestrator component
│       ├── app.html            ← wire all 8 components
│       └── app.spec.ts         ← update tests
```

---

## Task 1: Reorganize Folder Structure

**Goal:** Move the Angular project from the nested `teste-manager-frontend/teste-manager-frontend/` to the correct root level at `teste-manager-frontend/`.

All commands below run from the **repository root** (`task-manager/`).

- [ ] **Step 1: Copy Angular project files up one level**

```bash
cd "teste-manager-frontend"
cp -rf teste-manager-frontend/src .
cp -f teste-manager-frontend/angular.json .
cp -f teste-manager-frontend/package.json .
cp -f teste-manager-frontend/tsconfig.json .
cp -f teste-manager-frontend/tsconfig.app.json .
cp -f teste-manager-frontend/tsconfig.spec.json .
cp -f teste-manager-frontend/README.md .
cp -rf teste-manager-frontend/public . 2>/dev/null || true
```

- [ ] **Step 2: Remove the nested project folder and old node_modules**

```bash
# Still inside teste-manager-frontend/
rm -rf teste-manager-frontend
rm -rf node_modules
rm -f package-lock.json
```

- [ ] **Step 3: Install Angular dependencies**

```bash
# Still inside teste-manager-frontend/
npm install
```

Expected: `node_modules/` created, no errors. Angular 21 packages installed.

- [ ] **Step 4: Verify structure is correct**

```bash
ls -la
# Should show: src/ angular.json package.json tailwind.config.js tsconfig*.json
ls src/app/
# Should show: app.config.ts app.css app.html app.routes.ts app.spec.ts app.ts
```

- [ ] **Step 5: Commit**

```bash
cd ..   # back to task-manager/ root
git add teste-manager-frontend/
git commit -m "refactor: flatten Angular project to teste-manager-frontend root"
```

---

## Task 2: Install and Configure Tailwind CSS + highlight.js

All commands run from `teste-manager-frontend/`.

- [ ] **Step 1: Install dependencies**

```bash
cd teste-manager-frontend
npm install -D tailwindcss@^3.4.19 postcss autoprefixer
npm install highlight.js
```

- [ ] **Step 2: Update `tailwind.config.js`**

Replace the entire file with:

```js
// tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
      colors: {
        primary: '#18181B',
        'primary-hover': '#27272A',
      },
    },
  },
  plugins: [],
};
```

- [ ] **Step 3: Create `postcss.config.js`**

```js
// postcss.config.js
module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
};
```

- [ ] **Step 4: Update `src/styles.css`**

Replace the entire file with:

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

html {
  scroll-behavior: smooth;
}

body {
  font-family: var(--font-sans);
  color: var(--color-text-primary);
  background: #ffffff;
  -webkit-font-smoothing: antialiased;
}
```

- [ ] **Step 5: Update `src/index.html`**

Replace the entire file with:

```html
<!doctype html>
<html lang="pt-BR">
  <head>
    <meta charset="utf-8" />
    <title>Task Manager API</title>
    <base href="/" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="API REST para gestão de tarefas com Spring Boot — projeto de portfólio." />
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap"
      rel="stylesheet"
    />
  </head>
  <body>
    <app-root></app-root>
  </body>
</html>
```

- [ ] **Step 6: Verify Tailwind is working**

```bash
ng build --configuration development 2>&1 | tail -10
```

Expected: build completes without errors. If there's an error about PostCSS, make sure `postcss.config.js` is in the project root (same level as `angular.json`).

- [ ] **Step 7: Commit**

```bash
cd ..
git add teste-manager-frontend/
git commit -m "feat: install and configure Tailwind CSS 3 + highlight.js"
```

---

## Task 3: HeaderComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/header/header.ts`
- Create: `teste-manager-frontend/src/app/components/header/header.html`
- Create: `teste-manager-frontend/src/app/components/header/header.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/header/header.spec.ts
import { TestBed } from '@angular/core/testing';
import { Header } from './header';

describe('Header', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Header],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Header);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should have a GitHub link', () => {
    const fixture = TestBed.createComponent(Header);
    fixture.detectChanges();
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href*="github.com"]');
    expect(link).toBeTruthy();
  });

  it('should toggle mobile menu on button click', () => {
    const fixture = TestBed.createComponent(Header);
    fixture.detectChanges();
    const btn: HTMLButtonElement = fixture.nativeElement.querySelector('button[aria-label="Toggle menu"]');
    btn.click();
    fixture.detectChanges();
    const mobileMenu = fixture.nativeElement.querySelector('[data-testid="mobile-menu"]');
    expect(mobileMenu).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/header.spec.ts" 2>&1 | tail -15
```

Expected: error — `Cannot find module './header'`

- [ ] **Step 3: Create `header.ts`**

```typescript
// src/app/components/header/header.ts
import { Component, HostListener, signal } from '@angular/core';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.html',
})
export class Header {
  scrolled = signal(false);
  menuOpen = signal(false);

  @HostListener('window:scroll')
  onScroll(): void {
    this.scrolled.set(window.scrollY > 20);
  }

  toggleMenu(): void {
    this.menuOpen.set(!this.menuOpen());
  }
}
```

- [ ] **Step 4: Create `header.html`**

```html
<!-- src/app/components/header/header.html -->
<header
  class="fixed top-0 left-0 right-0 z-50 transition-all duration-200"
  [class]="scrolled()
    ? 'fixed top-0 left-0 right-0 z-50 transition-all duration-200 bg-white/90 backdrop-blur-sm border-b border-gray-200'
    : 'fixed top-0 left-0 right-0 z-50 transition-all duration-200 bg-transparent'"
>
  <div class="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">

    <!-- Logo -->
    <span class="font-semibold text-gray-900 text-sm tracking-tight">TaskManager API</span>

    <!-- Nav links (desktop) -->
    <nav class="hidden md:flex items-center gap-8">
      <a href="#features" class="text-sm text-gray-600 hover:text-gray-900 transition-colors duration-150">Features</a>
      <a href="#architecture" class="text-sm text-gray-600 hover:text-gray-900 transition-colors duration-150">Arquitetura</a>
      <a href="#stack" class="text-sm text-gray-600 hover:text-gray-900 transition-colors duration-150">Stack</a>
    </nav>

    <!-- CTA + hamburger -->
    <div class="flex items-center gap-3">
      <a
        href="https://github.com/MatheusFolster/taskmanager-api"
        target="_blank"
        rel="noopener noreferrer"
        class="hidden md:flex items-center gap-2 px-3 py-1.5 text-sm border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors duration-150"
      >
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
          <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
        </svg>
        Ver no GitHub
      </a>

      <button
        class="md:hidden p-2 text-gray-600 hover:text-gray-900"
        (click)="toggleMenu()"
        aria-label="Toggle menu"
      >
        @if (!menuOpen()) {
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        } @else {
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        }
      </button>
    </div>
  </div>

  <!-- Mobile menu -->
  @if (menuOpen()) {
    <div data-testid="mobile-menu" class="md:hidden border-t border-gray-200 bg-white px-6 py-4 flex flex-col gap-4">
      <a href="#features" class="text-sm text-gray-600 hover:text-gray-900" (click)="toggleMenu()">Features</a>
      <a href="#architecture" class="text-sm text-gray-600 hover:text-gray-900" (click)="toggleMenu()">Arquitetura</a>
      <a href="#stack" class="text-sm text-gray-600 hover:text-gray-900" (click)="toggleMenu()">Stack</a>
      <a
        href="https://github.com/MatheusFolster/taskmanager-api"
        target="_blank"
        rel="noopener noreferrer"
        class="text-sm font-medium text-gray-900"
      >Ver no GitHub →</a>
    </div>
  }
</header>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/header.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/header/
git commit -m "feat: add HeaderComponent with sticky nav and mobile menu"
```

---

## Task 4: HeroComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/hero/hero.ts`
- Create: `teste-manager-frontend/src/app/components/hero/hero.html`
- Create: `teste-manager-frontend/src/app/components/hero/hero.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/hero/hero.spec.ts
import { TestBed } from '@angular/core/testing';
import { Hero } from './hero';

describe('Hero', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Hero],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Hero);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render GitHub CTA link', () => {
    const fixture = TestBed.createComponent(Hero);
    fixture.detectChanges();
    const links: NodeListOf<HTMLAnchorElement> = fixture.nativeElement.querySelectorAll('a[href*="github.com"]');
    expect(links.length).toBeGreaterThan(0);
  });

  it('should render Swagger CTA link', () => {
    const fixture = TestBed.createComponent(Hero);
    fixture.detectChanges();
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href*="swagger"]');
    expect(link).toBeTruthy();
  });

  it('should render a code block', () => {
    const fixture = TestBed.createComponent(Hero);
    fixture.detectChanges();
    const pre = fixture.nativeElement.querySelector('pre');
    expect(pre).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/hero.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './hero'`

- [ ] **Step 3: Create `hero.ts`**

```typescript
// src/app/components/hero/hero.ts
import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import hljs from 'highlight.js/lib/core';
import http from 'highlight.js/lib/languages/http';
import json from 'highlight.js/lib/languages/json';

hljs.registerLanguage('http', http);
hljs.registerLanguage('json', json);

@Component({
  selector: 'app-hero',
  standalone: true,
  templateUrl: './hero.html',
})
export class Hero implements AfterViewInit {
  @ViewChild('codeBlock') codeBlock!: ElementRef<HTMLElement>;

  ngAfterViewInit(): void {
    if (this.codeBlock) {
      hljs.highlightElement(this.codeBlock.nativeElement);
    }
  }
}
```

- [ ] **Step 4: Create `hero.html`**

```html
<!-- src/app/components/hero/hero.html -->
<section class="relative min-h-screen flex items-center pt-20 pb-16 px-6 overflow-hidden">

  <!-- Grid background pattern -->
  <div
    class="absolute inset-0 -z-10"
    style="background-image: linear-gradient(#E5E7EB 1px, transparent 1px), linear-gradient(to right, #E5E7EB 1px, transparent 1px); background-size: 40px 40px; opacity: 0.4;"
  ></div>
  <!-- Fade overlay at bottom -->
  <div class="absolute bottom-0 left-0 right-0 h-32 -z-10 bg-gradient-to-t from-white to-transparent"></div>

  <div class="max-w-6xl mx-auto w-full grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">

    <!-- Left: text content -->
    <div class="flex flex-col gap-6">
      <!-- Badge -->
      <span class="inline-flex w-fit items-center gap-2 px-3 py-1 text-xs font-medium border border-gray-200 rounded-full text-gray-600 bg-white">
        <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span>
        Projeto de Portfólio · Spring Boot
      </span>

      <!-- Headline -->
      <h1 class="text-4xl lg:text-6xl font-bold text-gray-900 leading-tight tracking-tight">
        API de Gestão<br />de Tarefas
      </h1>

      <!-- Subheadline -->
      <p class="text-lg text-gray-500 leading-relaxed max-w-lg">
        API REST com hierarquia Projetos → Tarefas → Subtarefas. Autenticação JWT, cobertura de testes, arquitetura em camadas.
      </p>

      <!-- CTAs -->
      <div class="flex flex-wrap gap-3 pt-2">
        <a
          href="https://github.com/MatheusFolster/taskmanager-api"
          target="_blank"
          rel="noopener noreferrer"
          class="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-medium bg-gray-900 text-white rounded-md hover:bg-gray-800 transition-colors duration-150"
        >
          <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
          </svg>
          Ver no GitHub
        </a>
        <a
          href="http://localhost:8080/swagger-ui.html"
          target="_blank"
          rel="noopener noreferrer"
          class="inline-flex items-center gap-2 px-5 py-2.5 text-sm font-medium border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors duration-150"
        >
          Ver Swagger
          <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
          </svg>
        </a>
      </div>
    </div>

    <!-- Right: code block -->
    <div class="rounded-xl overflow-hidden border border-gray-800 shadow-2xl">
      <!-- Terminal header bar -->
      <div class="flex items-center gap-2 px-4 py-3 bg-gray-900 border-b border-gray-800">
        <span class="w-3 h-3 rounded-full bg-red-500/70"></span>
        <span class="w-3 h-3 rounded-full bg-yellow-500/70"></span>
        <span class="w-3 h-3 rounded-full bg-green-500/70"></span>
        <span class="ml-3 text-xs text-gray-500 font-mono">POST /auth/login</span>
      </div>
      <!-- Code -->
      <pre class="bg-gray-950 p-5 text-sm overflow-x-auto m-0"><code #codeBlock class="language-http">POST /auth/login HTTP/1.1
Content-Type: application/json

{
  "email": "dev&#64;example.com",
  "password": "secret123"
}

HTTP/1.1 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "dGhpcyBpcyBhIHNhbXBsZQ==",
  "email": "dev&#64;example.com",
  "expiresIn": 86400
}</code></pre>
    </div>

  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/hero.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 4 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/hero/
git commit -m "feat: add HeroComponent with code block and CTAs"
```

---

## Task 5: FeaturesComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/features/features.ts`
- Create: `teste-manager-frontend/src/app/components/features/features.html`
- Create: `teste-manager-frontend/src/app/components/features/features.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/features/features.spec.ts
import { TestBed } from '@angular/core/testing';
import { Features } from './features';

describe('Features', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Features],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Features);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render exactly 6 feature cards', () => {
    const fixture = TestBed.createComponent(Features);
    fixture.detectChanges();
    const cards = fixture.nativeElement.querySelectorAll('[data-testid="feature-card"]');
    expect(cards.length).toBe(6);
  });

  it('should have section id for anchor navigation', () => {
    const fixture = TestBed.createComponent(Features);
    fixture.detectChanges();
    const section = fixture.nativeElement.querySelector('#features');
    expect(section).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/features.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './features'`

- [ ] **Step 3: Create `features.ts`**

```typescript
// src/app/components/features/features.ts
import { Component } from '@angular/core';

interface Feature {
  title: string;
  description: string;
  icon: string; // SVG path d attribute
}

@Component({
  selector: 'app-features',
  standalone: true,
  templateUrl: './features.html',
})
export class Features {
  readonly features: Feature[] = [
    {
      title: 'Arquitetura em Camadas',
      description: 'Controller → Service → Repository. Separação clara de responsabilidades em cada camada.',
      icon: 'M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10',
    },
    {
      title: 'Autenticação JWT',
      description: 'Registro, login e refresh token com Spring Security. Rotas protegidas por usuário.',
      icon: 'M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z',
    },
    {
      title: 'Domínio Rico',
      description: 'Hierarquia Projetos → Tarefas → Subtarefas com status, prioridade, tags e progresso.',
      icon: 'M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01',
    },
    {
      title: 'Cobertura de Testes',
      description: '63 testes, 90% de cobertura. Unitários com Mockito e integração com MockMvc.',
      icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    },
    {
      title: 'Tratamento de Erros',
      description: 'GlobalExceptionHandler centralizado com respostas padronizadas para todos os casos.',
      icon: 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z',
    },
    {
      title: 'Documentação Swagger',
      description: 'Todos os endpoints documentados com OpenAPI 3. Acessível em /swagger-ui.html.',
      icon: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z',
    },
  ];
}
```

- [ ] **Step 4: Create `features.html`**

```html
<!-- src/app/components/features/features.html -->
<section id="features" class="py-24 px-6">
  <div class="max-w-6xl mx-auto">

    <!-- Section header -->
    <div class="mb-16 text-center">
      <h2 class="text-3xl font-bold text-gray-900 mb-4">Diferenciais Técnicos</h2>
      <p class="text-gray-500 max-w-xl mx-auto">
        Construído para demonstrar arquitetura Spring Boot profissional com código limpo e testável.
      </p>
    </div>

    <!-- Features grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      @for (feature of features; track feature.title) {
        <div
          data-testid="feature-card"
          class="p-6 border border-gray-200 rounded-xl bg-white hover:shadow-md transition-shadow duration-150 cursor-default"
        >
          <!-- Icon -->
          <div class="w-10 h-10 flex items-center justify-center rounded-lg bg-gray-100 mb-4">
            <svg class="w-5 h-5 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" [attr.d]="feature.icon"/>
            </svg>
          </div>
          <!-- Content -->
          <h3 class="font-semibold text-gray-900 mb-2">{{ feature.title }}</h3>
          <p class="text-sm text-gray-500 leading-relaxed">{{ feature.description }}</p>
        </div>
      }
    </div>

  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/features.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/features/
git commit -m "feat: add FeaturesComponent with 6 feature cards grid"
```

---

## Task 6: ArchitectureComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/architecture/architecture.ts`
- Create: `teste-manager-frontend/src/app/components/architecture/architecture.html`
- Create: `teste-manager-frontend/src/app/components/architecture/architecture.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/architecture/architecture.spec.ts
import { TestBed } from '@angular/core/testing';
import { Architecture } from './architecture';

describe('Architecture', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Architecture],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Architecture);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should have section id for anchor navigation', () => {
    const fixture = TestBed.createComponent(Architecture);
    fixture.detectChanges();
    const section = fixture.nativeElement.querySelector('#architecture');
    expect(section).toBeTruthy();
  });

  it('should render the SVG architecture diagram', () => {
    const fixture = TestBed.createComponent(Architecture);
    fixture.detectChanges();
    const svg = fixture.nativeElement.querySelector('svg[data-testid="arch-diagram"]');
    expect(svg).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/architecture.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './architecture'`

- [ ] **Step 3: Create `architecture.ts`**

```typescript
// src/app/components/architecture/architecture.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-architecture',
  standalone: true,
  templateUrl: './architecture.html',
})
export class Architecture {}
```

- [ ] **Step 4: Create `architecture.html`**

```html
<!-- src/app/components/architecture/architecture.html -->
<section id="architecture" class="py-24 px-6 bg-gray-50">
  <div class="max-w-6xl mx-auto">

    <!-- Section header -->
    <div class="mb-16 text-center">
      <h2 class="text-3xl font-bold text-gray-900 mb-4">Arquitetura</h2>
      <p class="text-gray-500 max-w-xl mx-auto">
        Separação clara entre camadas com fluxo unidirecional de dependências.
      </p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-start">

      <!-- Left: flow diagram -->
      <div>
        <p class="text-xs font-medium text-gray-500 uppercase tracking-wider mb-6">Fluxo de Requisição</p>
        <svg
          data-testid="arch-diagram"
          viewBox="0 0 480 280"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          class="w-full"
        >
          <defs>
            <marker id="arrowhead" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto">
              <polygon points="0 0, 8 3, 0 6" fill="#9CA3AF"/>
            </marker>
          </defs>

          <!-- HTTP Client box -->
          <rect x="160" y="10" width="160" height="44" rx="8" fill="#F3F4F6" stroke="#E5E7EB" stroke-width="1.5"/>
          <text x="240" y="28" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="11" fill="#374151">HTTP Client</text>
          <text x="240" y="44" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="10" fill="#9CA3AF">JWT Bearer Token</text>

          <!-- Arrow down -->
          <line x1="240" y1="54" x2="240" y2="74" stroke="#D1D5DB" stroke-width="1.5" marker-end="url(#arrowhead)"/>

          <!-- JwtFilter box -->
          <rect x="100" y="74" width="280" height="44" rx="8" fill="#FEF3C7" stroke="#FDE68A" stroke-width="1.5"/>
          <text x="240" y="92" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="11" fill="#92400E">JwtAuthenticationFilter</text>
          <text x="240" y="108" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="10" fill="#B45309">Valida token · Autentica usuário</text>

          <!-- Arrow down -->
          <line x1="240" y1="118" x2="240" y2="138" stroke="#D1D5DB" stroke-width="1.5" marker-end="url(#arrowhead)"/>

          <!-- Controller box -->
          <rect x="130" y="138" width="220" height="44" rx="8" fill="#F9FAFB" stroke="#E5E7EB" stroke-width="1.5"/>
          <text x="240" y="156" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="11" fill="#374151">Controller</text>
          <text x="240" y="172" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="10" fill="#9CA3AF">Valida input · Mapeia DTOs</text>

          <!-- Arrow down -->
          <line x1="240" y1="182" x2="240" y2="202" stroke="#D1D5DB" stroke-width="1.5" marker-end="url(#arrowhead)"/>

          <!-- Service box -->
          <rect x="130" y="202" width="220" height="44" rx="8" fill="#F9FAFB" stroke="#E5E7EB" stroke-width="1.5"/>
          <text x="240" y="220" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="11" fill="#374151">Service</text>
          <text x="240" y="236" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="10" fill="#9CA3AF">Lógica de negócio</text>

          <!-- Arrow down -->
          <line x1="240" y1="246" x2="240" y2="266" stroke="#D1D5DB" stroke-width="1.5" marker-end="url(#arrowhead)"/>

          <!-- Repository + DB box -->
          <rect x="100" y="266" width="280" height="14" rx="4" fill="#DBEAFE" stroke="#BFDBFE" stroke-width="1.5"/>
          <text x="240" y="277" text-anchor="middle" font-family="JetBrains Mono, monospace" font-size="10" fill="#1E40AF">Repository → PostgreSQL</text>
        </svg>
      </div>

      <!-- Right: file tree -->
      <div>
        <p class="text-xs font-medium text-gray-500 uppercase tracking-wider mb-6">Estrutura de Pacotes</p>
        <div class="bg-gray-900 rounded-xl p-6 overflow-x-auto">
          <pre class="text-sm text-gray-300 font-mono m-0 leading-relaxed">
<span class="text-blue-400">com.taskmanager</span>
├── <span class="text-yellow-400">config/</span>          <span class="text-gray-500"># Security, OpenAPI, JWT</span>
├── <span class="text-yellow-400">controller/</span>      <span class="text-gray-500"># REST endpoints</span>
├── <span class="text-yellow-400">service/</span>
│   └── <span class="text-yellow-400">impl/</span>        <span class="text-gray-500"># Regras de negócio</span>
├── <span class="text-yellow-400">repository/</span>      <span class="text-gray-500"># Spring Data JPA</span>
├── <span class="text-yellow-400">entity/</span>
│   └── <span class="text-yellow-400">enums/</span>       <span class="text-gray-500"># Status, Priority</span>
├── <span class="text-yellow-400">dto/</span>
│   ├── <span class="text-yellow-400">auth/</span>        <span class="text-gray-500"># Login, Register</span>
│   ├── <span class="text-yellow-400">project/</span>
│   ├── <span class="text-yellow-400">task/</span>
│   └── <span class="text-yellow-400">subtask/</span>
├── <span class="text-yellow-400">mapper/</span>          <span class="text-gray-500"># MapStruct</span>
├── <span class="text-yellow-400">security/</span>        <span class="text-gray-500"># JWT filter</span>
└── <span class="text-yellow-400">exception/</span>       <span class="text-gray-500"># Global handler</span></pre>
        </div>
      </div>

    </div>
  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/architecture.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/architecture/
git commit -m "feat: add ArchitectureComponent with flow diagram and file tree"
```

---

## Task 7: TechStackComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/tech-stack/tech-stack.ts`
- Create: `teste-manager-frontend/src/app/components/tech-stack/tech-stack.html`
- Create: `teste-manager-frontend/src/app/components/tech-stack/tech-stack.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/tech-stack/tech-stack.spec.ts
import { TestBed } from '@angular/core/testing';
import { TechStack } from './tech-stack';

describe('TechStack', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechStack],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TechStack);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should have section id for anchor navigation', () => {
    const fixture = TestBed.createComponent(TechStack);
    fixture.detectChanges();
    const section = fixture.nativeElement.querySelector('#stack');
    expect(section).toBeTruthy();
  });

  it('should render at least 10 tech badges', () => {
    const fixture = TestBed.createComponent(TechStack);
    fixture.detectChanges();
    const badges = fixture.nativeElement.querySelectorAll('[data-testid="tech-badge"]');
    expect(badges.length).toBeGreaterThanOrEqual(10);
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/tech-stack.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './tech-stack'`

- [ ] **Step 3: Create `tech-stack.ts`**

```typescript
// src/app/components/tech-stack/tech-stack.ts
import { Component } from '@angular/core';

interface TechGroup {
  category: string;
  items: string[];
}

@Component({
  selector: 'app-tech-stack',
  standalone: true,
  templateUrl: './tech-stack.html',
})
export class TechStack {
  readonly groups: TechGroup[] = [
    {
      category: 'Backend',
      items: ['Java 21', 'Spring Boot 3.4', 'Spring Security', 'JPA / Hibernate', 'Flyway'],
    },
    {
      category: 'Banco de Dados',
      items: ['PostgreSQL', 'H2 (testes)'],
    },
    {
      category: 'Testes',
      items: ['JUnit 5', 'Mockito', 'MockMvc'],
    },
    {
      category: 'Documentação',
      items: ['Swagger / OpenAPI 3', 'MapStruct'],
    },
    {
      category: 'Frontend',
      items: ['Angular 21', 'Tailwind CSS', 'highlight.js'],
    },
    {
      category: 'DevOps',
      items: ['Docker Compose', 'GitHub Actions'],
    },
  ];
}
```

- [ ] **Step 4: Create `tech-stack.html`**

```html
<!-- src/app/components/tech-stack/tech-stack.html -->
<section id="stack" class="py-24 px-6">
  <div class="max-w-6xl mx-auto">

    <!-- Section header -->
    <div class="mb-16 text-center">
      <h2 class="text-3xl font-bold text-gray-900 mb-4">Stack Tecnológica</h2>
      <p class="text-gray-500 max-w-xl mx-auto">
        Tecnologias escolhidas para demonstrar profundidade técnica e boas práticas.
      </p>
    </div>

    <!-- Groups -->
    <div class="flex flex-col gap-8">
      @for (group of groups; track group.category) {
        <div class="flex flex-col sm:flex-row sm:items-start gap-4">
          <!-- Category label -->
          <span class="text-xs font-medium text-gray-400 uppercase tracking-wider w-32 shrink-0 pt-1">
            {{ group.category }}
          </span>
          <!-- Badges -->
          <div class="flex flex-wrap gap-2">
            @for (item of group.items; track item) {
              <span
                data-testid="tech-badge"
                class="inline-flex items-center px-3 py-1 text-xs font-medium bg-gray-100 text-gray-700 border border-gray-200 rounded-full"
              >{{ item }}</span>
            }
          </div>
        </div>
      }
    </div>

  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/tech-stack.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/tech-stack/
git commit -m "feat: add TechStackComponent with grouped technology badges"
```

---

## Task 8: HowItWorksComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/how-it-works/how-it-works.ts`
- Create: `teste-manager-frontend/src/app/components/how-it-works/how-it-works.html`
- Create: `teste-manager-frontend/src/app/components/how-it-works/how-it-works.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/how-it-works/how-it-works.spec.ts
import { TestBed } from '@angular/core/testing';
import { HowItWorks } from './how-it-works';

describe('HowItWorks', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HowItWorks],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(HowItWorks);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render exactly 4 steps', () => {
    const fixture = TestBed.createComponent(HowItWorks);
    fixture.detectChanges();
    const steps = fixture.nativeElement.querySelectorAll('[data-testid="step"]');
    expect(steps.length).toBe(4);
  });

  it('should render a code example block', () => {
    const fixture = TestBed.createComponent(HowItWorks);
    fixture.detectChanges();
    const pre = fixture.nativeElement.querySelector('pre');
    expect(pre).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/how-it-works.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './how-it-works'`

- [ ] **Step 3: Create `how-it-works.ts`**

```typescript
// src/app/components/how-it-works/how-it-works.ts
import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import hljs from 'highlight.js/lib/core';
import http from 'highlight.js/lib/languages/http';
import json from 'highlight.js/lib/languages/json';

hljs.registerLanguage('http', http);
hljs.registerLanguage('json', json);

interface Step {
  number: number;
  title: string;
  description: string;
}

@Component({
  selector: 'app-how-it-works',
  standalone: true,
  templateUrl: './how-it-works.html',
})
export class HowItWorks implements AfterViewInit {
  @ViewChild('codeExample') codeExample!: ElementRef<HTMLElement>;

  readonly steps: Step[] = [
    { number: 1, title: 'Autenticar', description: 'Registre-se e obtenha um JWT Bearer Token.' },
    { number: 2, title: 'Criar Projeto', description: 'Agrupe suas tarefas em projetos.' },
    { number: 3, title: 'Adicionar Tarefas', description: 'Crie tarefas com prioridade, status e tags.' },
    { number: 4, title: 'Gerenciar Subtarefas', description: 'Detalhe o progresso com subtarefas.' },
  ];

  ngAfterViewInit(): void {
    if (this.codeExample) {
      hljs.highlightElement(this.codeExample.nativeElement);
    }
  }
}
```

- [ ] **Step 4: Create `how-it-works.html`**

```html
<!-- src/app/components/how-it-works/how-it-works.html -->
<section class="py-24 px-6 bg-gray-50">
  <div class="max-w-6xl mx-auto">

    <!-- Section header -->
    <div class="mb-16 text-center">
      <h2 class="text-3xl font-bold text-gray-900 mb-4">Como Funciona</h2>
      <p class="text-gray-500 max-w-xl mx-auto">
        Quatro chamadas para organizar qualquer fluxo de trabalho.
      </p>
    </div>

    <!-- Steps -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-16">
      @for (step of steps; track step.number) {
        <div data-testid="step" class="relative">
          <!-- Connector line (hidden on last) -->
          @if (step.number < 4) {
            <div class="hidden lg:block absolute top-5 left-full w-full h-px bg-gray-200 z-0" style="width: calc(100% - 2rem); left: calc(100% - 0.5rem)"></div>
          }
          <div class="relative z-10">
            <!-- Number badge -->
            <div class="w-10 h-10 flex items-center justify-center rounded-full bg-gray-900 text-white text-sm font-bold mb-4">
              {{ step.number }}
            </div>
            <h3 class="font-semibold text-gray-900 mb-1">{{ step.title }}</h3>
            <p class="text-sm text-gray-500">{{ step.description }}</p>
          </div>
        </div>
      }
    </div>

    <!-- Code example -->
    <div class="rounded-xl overflow-hidden border border-gray-800">
      <div class="flex items-center gap-2 px-4 py-3 bg-gray-900 border-b border-gray-800">
        <span class="w-3 h-3 rounded-full bg-red-500/70"></span>
        <span class="w-3 h-3 rounded-full bg-yellow-500/70"></span>
        <span class="w-3 h-3 rounded-full bg-green-500/70"></span>
        <span class="ml-3 text-xs text-gray-500 font-mono">POST /projects/{id}/tasks</span>
      </div>
      <pre class="bg-gray-950 p-5 text-sm overflow-x-auto m-0"><code #codeExample class="language-http">POST /projects/42/tasks HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "title": "Implementar autenticação OAuth",
  "description": "Adicionar login com Google",
  "priority": "HIGH",
  "status": "TODO",
  "dueDate": "2026-05-01",
  "tags": ["auth", "backend"]
}

HTTP/1.1 201 Created
Location: /tasks/87</code></pre>
    </div>

  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/how-it-works.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/how-it-works/
git commit -m "feat: add HowItWorksComponent with 4 steps and code example"
```

---

## Task 9: CtaFinalComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/cta-final/cta-final.ts`
- Create: `teste-manager-frontend/src/app/components/cta-final/cta-final.html`
- Create: `teste-manager-frontend/src/app/components/cta-final/cta-final.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/cta-final/cta-final.spec.ts
import { TestBed } from '@angular/core/testing';
import { CtaFinal } from './cta-final';

describe('CtaFinal', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CtaFinal],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(CtaFinal);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render GitHub CTA', () => {
    const fixture = TestBed.createComponent(CtaFinal);
    fixture.detectChanges();
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href*="github.com"]');
    expect(link).toBeTruthy();
    expect(link.textContent).toContain('GitHub');
  });

  it('should render Swagger CTA', () => {
    const fixture = TestBed.createComponent(CtaFinal);
    fixture.detectChanges();
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href*="swagger"]');
    expect(link).toBeTruthy();
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/cta-final.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './cta-final'`

- [ ] **Step 3: Create `cta-final.ts`**

```typescript
// src/app/components/cta-final/cta-final.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-cta-final',
  standalone: true,
  templateUrl: './cta-final.html',
})
export class CtaFinal {}
```

- [ ] **Step 4: Create `cta-final.html`**

```html
<!-- src/app/components/cta-final/cta-final.html -->
<section class="py-24 px-6 bg-gray-900">
  <div class="max-w-2xl mx-auto text-center flex flex-col items-center gap-6">

    <h2 class="text-3xl lg:text-4xl font-bold text-white leading-tight">
      Pronto para explorar o código?
    </h2>

    <p class="text-gray-400 text-lg max-w-md">
      Veja a implementação completa, os testes e a documentação no repositório.
    </p>

    <div class="flex flex-wrap justify-center gap-4 pt-2">
      <a
        href="https://github.com/MatheusFolster/taskmanager-api"
        target="_blank"
        rel="noopener noreferrer"
        class="inline-flex items-center gap-2 px-6 py-3 text-sm font-medium bg-white text-gray-900 rounded-md hover:bg-gray-100 transition-colors duration-150"
      >
        <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
          <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
        </svg>
        Ver código no GitHub
      </a>
      <a
        href="http://localhost:8080/swagger-ui.html"
        target="_blank"
        rel="noopener noreferrer"
        class="inline-flex items-center gap-2 px-6 py-3 text-sm font-medium border border-gray-600 text-gray-300 rounded-md hover:bg-gray-800 hover:border-gray-500 transition-colors duration-150"
      >
        Explorar Swagger
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"/>
        </svg>
      </a>
    </div>

  </div>
</section>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/cta-final.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/cta-final/
git commit -m "feat: add CtaFinalComponent with dark background and CTAs"
```

---

## Task 10: FooterComponent

**Files:**
- Create: `teste-manager-frontend/src/app/components/footer/footer.ts`
- Create: `teste-manager-frontend/src/app/components/footer/footer.html`
- Create: `teste-manager-frontend/src/app/components/footer/footer.spec.ts`

- [ ] **Step 1: Write the failing test**

```typescript
// src/app/components/footer/footer.spec.ts
import { TestBed } from '@angular/core/testing';
import { Footer } from './footer';

describe('Footer', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Footer],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Footer);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render GitHub link', () => {
    const fixture = TestBed.createComponent(Footer);
    fixture.detectChanges();
    const link: HTMLAnchorElement = fixture.nativeElement.querySelector('a[href*="github.com"]');
    expect(link).toBeTruthy();
  });

  it('should render current year in copyright', () => {
    const fixture = TestBed.createComponent(Footer);
    fixture.detectChanges();
    const text: string = fixture.nativeElement.textContent;
    expect(text).toContain(new Date().getFullYear().toString());
  });
});
```

- [ ] **Step 2: Run to verify it fails**

```bash
cd teste-manager-frontend
ng test --include="**/footer.spec.ts" 2>&1 | tail -10
```

Expected: error — `Cannot find module './footer'`

- [ ] **Step 3: Create `footer.ts`**

```typescript
// src/app/components/footer/footer.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  templateUrl: './footer.html',
})
export class Footer {
  readonly year = new Date().getFullYear();
}
```

- [ ] **Step 4: Create `footer.html`**

```html
<!-- src/app/components/footer/footer.html -->
<footer class="border-t border-gray-200 py-8 px-6">
  <div class="max-w-6xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4">

    <!-- Left: project name -->
    <div>
      <span class="text-sm font-medium text-gray-900">TaskManager API</span>
      <span class="text-gray-400 mx-2">·</span>
      <span class="text-sm text-gray-500">Projeto de Portfólio — Spring Boot</span>
    </div>

    <!-- Right: links + copyright -->
    <div class="flex items-center gap-6">
      <a
        href="https://github.com/MatheusFolster/taskmanager-api"
        target="_blank"
        rel="noopener noreferrer"
        class="text-sm text-gray-500 hover:text-gray-900 transition-colors duration-150"
      >GitHub</a>
      <a
        href="http://localhost:8080/swagger-ui.html"
        target="_blank"
        rel="noopener noreferrer"
        class="text-sm text-gray-500 hover:text-gray-900 transition-colors duration-150"
      >Swagger</a>
      <a
        href="https://github.com/MatheusFolster/taskmanager-api#readme"
        target="_blank"
        rel="noopener noreferrer"
        class="text-sm text-gray-500 hover:text-gray-900 transition-colors duration-150"
      >README</a>
      <span class="text-sm text-gray-400">© {{ year }}</span>
    </div>

  </div>
</footer>
```

- [ ] **Step 5: Run tests to verify they pass**

```bash
ng test --include="**/footer.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 6: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/components/footer/
git commit -m "feat: add FooterComponent with links and copyright"
```

---

## Task 11: AppComponent — Wire All Components

**Files:**
- Modify: `teste-manager-frontend/src/app/app.ts`
- Modify: `teste-manager-frontend/src/app/app.html`
- Modify: `teste-manager-frontend/src/app/app.spec.ts`

- [ ] **Step 1: Update `app.spec.ts`**

```typescript
// src/app/app.spec.ts
import { TestBed } from '@angular/core/testing';
import { App } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render header', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const header = fixture.nativeElement.querySelector('app-header');
    expect(header).toBeTruthy();
  });

  it('should render all 8 sections', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const selectors = ['app-header', 'app-hero', 'app-features', 'app-architecture', 'app-tech-stack', 'app-how-it-works', 'app-cta-final', 'app-footer'];
    for (const sel of selectors) {
      expect(fixture.nativeElement.querySelector(sel)).withContext(`Expected <${sel}> to exist`).toBeTruthy();
    }
  });
});
```

- [ ] **Step 2: Run to verify the existing test fails (title test will fail)**

```bash
cd teste-manager-frontend
ng test --include="**/app.spec.ts" 2>&1 | tail -10
```

Expected: 2 tests fail — `render header` and `render all 8 sections`

- [ ] **Step 3: Update `app.ts`**

```typescript
// src/app/app.ts
import { Component } from '@angular/core';
import { Header } from './components/header/header';
import { Hero } from './components/hero/hero';
import { Features } from './components/features/features';
import { Architecture } from './components/architecture/architecture';
import { TechStack } from './components/tech-stack/tech-stack';
import { HowItWorks } from './components/how-it-works/how-it-works';
import { CtaFinal } from './components/cta-final/cta-final';
import { Footer } from './components/footer/footer';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    Header,
    Hero,
    Features,
    Architecture,
    TechStack,
    HowItWorks,
    CtaFinal,
    Footer,
  ],
  templateUrl: './app.html',
})
export class App {}
```

- [ ] **Step 4: Update `app.html`**

```html
<!-- src/app/app.html -->
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

- [ ] **Step 5: Also remove the unused `app.css` import from `app.ts` if present — `styleUrl` can be removed since the file has no content needed**

Check if `app.css` has content. If it's empty, just remove `styleUrl: './app.css'` from the decorator in `app.ts`. The file above does not include it, so this is already handled.

- [ ] **Step 6: Run tests to verify they pass**

```bash
ng test --include="**/app.spec.ts" 2>&1 | tail -10
```

Expected: `Tests 3 passed`

- [ ] **Step 7: Commit**

```bash
cd ..
git add teste-manager-frontend/src/app/app.ts teste-manager-frontend/src/app/app.html teste-manager-frontend/src/app/app.spec.ts
git commit -m "feat: wire all 8 landing page components in AppComponent"
```

---

## Task 12: Final Verification

- [ ] **Step 1: Run the full test suite**

```bash
cd teste-manager-frontend
ng test --watch=false 2>&1 | tail -20
```

Expected: all tests pass, 0 failures. There should be ~27 tests (3 per component × 8 components + 3 app tests).

- [ ] **Step 2: Run a production build**

```bash
ng build 2>&1 | tail -15
```

Expected: build completes successfully. Output in `dist/`.

- [ ] **Step 3: Serve locally and do a visual check**

```bash
ng serve --open
```

Open `http://localhost:4200` in the browser and verify:
- [ ] Nav is visible and links scroll to correct sections
- [ ] Hero shows the code block with syntax highlighting
- [ ] Features shows 6 cards in a 3×2 grid
- [ ] Architecture shows the flow diagram and file tree
- [ ] TechStack shows grouped badges
- [ ] HowItWorks shows 4 numbered steps and a code snippet
- [ ] CtaFinal has dark background and two CTA buttons
- [ ] Footer has links and copyright year
- [ ] On mobile (resize to < 768px): nav collapses to hamburger, hero stacks vertically

- [ ] **Step 4: Final commit**

```bash
cd ..
git add teste-manager-frontend/
git commit -m "feat: complete landing page — all 8 sections built and tested"
```

---

## Reference URLs

| Destination | URL |
|---|---|
| GitHub | `https://github.com/MatheusFolster/taskmanager-api` |
| Swagger | `http://localhost:8080/swagger-ui.html` *(placeholder)* |

---

## Notes for Implementer

- All Angular commands run from `teste-manager-frontend/` (after Task 1 reorganization)
- Git commits run from the repository root `task-manager/`
- Angular 21 uses the `@if` / `@for` control flow syntax — no need to import `NgIf` / `NgFor`
- `NgClass` must be imported explicitly in components that use `[ngClass]` — the Header component uses direct `[class]` binding instead to avoid the import
- highlight.js is a direct (non-dev) dependency because it's used at runtime
- The `@` symbol in email addresses inside Angular templates must be escaped as `&#64;` to avoid the Angular template parser treating it as a decorator
