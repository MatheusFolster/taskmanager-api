# Frontend Angular — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Criar o frontend Angular que consome a Task Manager API, com telas de autenticação, projetos, tarefas e subtarefas.

**Architecture:** Standalone components (Angular 19+), serviços simples por recurso sem estado global, interceptor JWT automático com refresh. Backend precisa de CORS habilitado para `localhost:4200`.

**Tech Stack:** Angular 19, Tailwind CSS, PrimeNG 18 (@primeng/themes), TypeScript.

---

## Mapa de Arquivos

### Backend (modificações)
- Modify: `task-manager-api-backend/src/main/java/com/taskmanager/config/SecurityConfig.java`

### Frontend (criados do zero)
```
teste-manager-frontend/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── models/
│   │   │   │   ├── auth.model.ts
│   │   │   │   ├── project.model.ts
│   │   │   │   ├── task.model.ts
│   │   │   │   └── subtask.model.ts
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts
│   │   │   │   ├── project.service.ts
│   │   │   │   ├── task.service.ts
│   │   │   │   └── subtask.service.ts
│   │   │   ├── interceptors/
│   │   │   │   └── auth.interceptor.ts
│   │   │   └── guards/
│   │   │       └── auth.guard.ts
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   │   ├── login/login.component.ts
│   │   │   │   └── register/register.component.ts
│   │   │   ├── projects/
│   │   │   │   └── projects-list/projects-list.component.ts
│   │   │   ├── tasks/
│   │   │   │   └── tasks-list/tasks-list.component.ts
│   │   │   └── task-detail/task-detail.component.ts
│   │   ├── shared/components/navbar/navbar.component.ts
│   │   ├── app.component.ts
│   │   ├── app.routes.ts
│   │   └── app.config.ts
│   ├── environments/environment.ts
│   └── styles.css
└── tailwind.config.js
```

---

## Task 1: CORS no Backend

**Files:**
- Modify: `task-manager-api-backend/src/main/java/com/taskmanager/config/SecurityConfig.java`

- [ ] **Passo 1: Adicionar CORS ao SecurityConfig**

Adicionar imports e o bean `corsConfigurationSource`. Substituir o conteúdo de `SecurityConfig.java`:

```java
package com.taskmanager.config;

import com.taskmanager.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/h2-console/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Location"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

- [ ] **Passo 2: Verificar que os testes continuam passando**

```bash
cd task-manager-api-backend
./mvnw test
```

Esperado: `Tests run: 63, Failures: 0, Errors: 0`

- [ ] **Passo 3: Commit**

```bash
git add task-manager-api-backend/src/main/java/com/taskmanager/config/SecurityConfig.java
git commit -m "feat: enable CORS for Angular frontend on localhost:4200"
```

---

## Task 2: Scaffold Angular + Tailwind + PrimeNG

**Files:**
- Create: `teste-manager-frontend/` (scaffold completo)
- Create: `teste-manager-frontend/tailwind.config.js`
- Modify: `teste-manager-frontend/src/styles.css`

- [ ] **Passo 1: Criar o projeto Angular**

```bash
cd "C:\Users\Mathe\OneDrive\Desktop\Programação\Projetos\task-manager"
npx @angular/cli new teste-manager-frontend --routing --style=css --skip-git
```

Quando perguntado sobre SSR: **No**.

- [ ] **Passo 2: Instalar Tailwind CSS**

```bash
cd teste-manager-frontend
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init
```

- [ ] **Passo 3: Instalar PrimeNG**

```bash
npm install primeng @primeng/themes primeicons
```

- [ ] **Passo 4: Configurar `tailwind.config.js`**

Substituir o conteúdo gerado por:

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
        mono: ['JetBrains Mono', 'Fira Code', 'monospace'],
      },
    },
  },
  plugins: [],
}
```

- [ ] **Passo 5: Configurar `src/styles.css`**

```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
@import 'primeicons/primeicons.css';

@tailwind base;
@tailwind components;
@tailwind utilities;

* {
  box-sizing: border-box;
}

body {
  font-family: 'Inter', system-ui, sans-serif;
  background-color: #F9FAFB;
  color: #111827;
  margin: 0;
}
```

- [ ] **Passo 6: Verificar que o projeto compila**

```bash
ng build
```

Esperado: `Application bundle generation complete.`

- [ ] **Passo 7: Commit**

```bash
git add teste-manager-frontend/
git commit -m "chore: scaffold Angular project with Tailwind and PrimeNG"
```

---

## Task 3: Environment, Models e App Config

**Files:**
- Create: `teste-manager-frontend/src/environments/environment.ts`
- Create: `teste-manager-frontend/src/app/core/models/auth.model.ts`
- Create: `teste-manager-frontend/src/app/core/models/project.model.ts`
- Create: `teste-manager-frontend/src/app/core/models/task.model.ts`
- Create: `teste-manager-frontend/src/app/core/models/subtask.model.ts`
- Modify: `teste-manager-frontend/src/app/app.component.ts`

- [ ] **Passo 1: Criar `src/environments/environment.ts`**

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

- [ ] **Passo 2: Criar `src/app/core/models/auth.model.ts`**

```typescript
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}
```

- [ ] **Passo 3: Criar `src/app/core/models/project.model.ts`**

```typescript
export type ProjectStatus = 'ACTIVE' | 'ARCHIVED';

export interface Project {
  id: number;
  name: string;
  description: string;
  status: ProjectStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreateProjectRequest {
  name: string;
  description?: string;
}

export interface UpdateProjectRequest {
  name: string;
  description?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
```

- [ ] **Passo 4: Criar `src/app/core/models/task.model.ts`**

```typescript
export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE' | 'CANCELLED';
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';

export interface Task {
  id: number;
  projectId: number;
  title: string;
  description?: string;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate?: string;
  tags: string[];
  progress: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  priority: TaskPriority;
  dueDate?: string;
  tags?: string[];
}

export interface UpdateTaskStatusRequest {
  status: TaskStatus;
}
```

- [ ] **Passo 5: Criar `src/app/core/models/subtask.model.ts`**

```typescript
export interface Subtask {
  id: number;
  taskId: number;
  title: string;
  completed: boolean;
  createdAt: string;
}

export interface CreateSubtaskRequest {
  title: string;
}
```

- [ ] **Passo 6: Substituir `src/app/app.component.ts`**

```typescript
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: '<router-outlet />'
})
export class AppComponent {}
```

- [ ] **Passo 7: Commit**

```bash
git add teste-manager-frontend/src/
git commit -m "feat: add models and environment config"
```

---

## Task 4: AuthService

**Files:**
- Create: `teste-manager-frontend/src/app/core/services/auth.service.ts`

- [ ] **Passo 1: Criar `src/app/core/services/auth.service.ts`**

```typescript
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AuthResponse,
  LoginRequest,
  RefreshTokenRequest,
  RegisterRequest
} from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  login(request: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(
      tap(response => this.saveTokens(response))
    );
  }

  register(request: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request).pipe(
      tap(response => this.saveTokens(response))
    );
  }

  refresh() {
    const body: RefreshTokenRequest = {
      refreshToken: localStorage.getItem('refreshToken') ?? ''
    };
    return this.http.post<AuthResponse>(`${this.baseUrl}/refresh`, body).pipe(
      tap(response => this.saveTokens(response))
    );
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  private saveTokens(response: AuthResponse): void {
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
  }
}
```

- [ ] **Passo 2: Commit**

```bash
git add teste-manager-frontend/src/app/core/services/auth.service.ts
git commit -m "feat: add AuthService"
```

---

## Task 5: ProjectService, TaskService, SubtaskService

**Files:**
- Create: `teste-manager-frontend/src/app/core/services/project.service.ts`
- Create: `teste-manager-frontend/src/app/core/services/task.service.ts`
- Create: `teste-manager-frontend/src/app/core/services/subtask.service.ts`

- [ ] **Passo 1: Criar `src/app/core/services/project.service.ts`**

```typescript
import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {
  CreateProjectRequest,
  PageResponse,
  Project,
  ProjectStatus,
  UpdateProjectRequest
} from '../models/project.model';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/projects`;

  list(status?: ProjectStatus) {
    let params = new HttpParams().set('size', '50');
    if (status) params = params.set('status', status);
    return this.http.get<PageResponse<Project>>(this.baseUrl, { params });
  }

  create(request: CreateProjectRequest) {
    return this.http.post<Project>(this.baseUrl, request);
  }

  update(id: number, request: UpdateProjectRequest) {
    return this.http.put<Project>(`${this.baseUrl}/${id}`, request);
  }

  archive(id: number) {
    return this.http.patch<Project>(`${this.baseUrl}/${id}/archive`, {});
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
```

- [ ] **Passo 2: Criar `src/app/core/services/task.service.ts`**

```typescript
import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {
  CreateTaskRequest,
  PageResponse,
  Task,
  TaskPriority,
  TaskStatus,
  UpdateTaskStatusRequest
} from '../models/task.model';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  listByProject(projectId: number, filters?: { status?: TaskStatus; priority?: TaskPriority }) {
    let params = new HttpParams().set('size', '50');
    if (filters?.status) params = params.set('status', filters.status);
    if (filters?.priority) params = params.set('priority', filters.priority);
    return this.http.get<PageResponse<Task>>(
      `${this.baseUrl}/projects/${projectId}/tasks`, { params }
    );
  }

  getById(id: number) {
    return this.http.get<Task>(`${this.baseUrl}/tasks/${id}`);
  }

  create(projectId: number, request: CreateTaskRequest) {
    return this.http.post<Task>(`${this.baseUrl}/projects/${projectId}/tasks`, request);
  }

  updateStatus(id: number, request: UpdateTaskStatusRequest) {
    return this.http.patch<Task>(`${this.baseUrl}/tasks/${id}/status`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/tasks/${id}`);
  }
}
```

- [ ] **Passo 3: Criar `src/app/core/services/subtask.service.ts`**

```typescript
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CreateSubtaskRequest, Subtask } from '../models/subtask.model';

@Injectable({ providedIn: 'root' })
export class SubtaskService {
  private http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  listByTask(taskId: number) {
    return this.http.get<Subtask[]>(`${this.baseUrl}/tasks/${taskId}/subtasks`);
  }

  create(taskId: number, request: CreateSubtaskRequest) {
    return this.http.post<Subtask>(`${this.baseUrl}/tasks/${taskId}/subtasks`, request);
  }

  complete(id: number) {
    return this.http.patch<Subtask>(`${this.baseUrl}/subtasks/${id}/complete`, {});
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.baseUrl}/subtasks/${id}`);
  }
}
```

- [ ] **Passo 4: Commit**

```bash
git add teste-manager-frontend/src/app/core/services/
git commit -m "feat: add ProjectService, TaskService and SubtaskService"
```

---

## Task 6: AuthInterceptor, AuthGuard e App Config

**Files:**
- Create: `teste-manager-frontend/src/app/core/interceptors/auth.interceptor.ts`
- Create: `teste-manager-frontend/src/app/core/guards/auth.guard.ts`
- Modify: `teste-manager-frontend/src/app/app.routes.ts`
- Modify: `teste-manager-frontend/src/app/app.config.ts`

- [ ] **Passo 1: Criar `src/app/core/interceptors/auth.interceptor.ts`**

```typescript
import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/auth/')) {
        return authService.refresh().pipe(
          switchMap(() => {
            const newToken = authService.getAccessToken();
            const retried = req.clone({
              setHeaders: { Authorization: `Bearer ${newToken}` }
            });
            return next(retried);
          }),
          catchError(() => {
            authService.logout();
            return throwError(() => error);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
```

- [ ] **Passo 2: Criar `src/app/core/guards/auth.guard.ts`**

```typescript
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.isLoggedIn() ? true : router.createUrlTree(['/login']);
};
```

- [ ] **Passo 3: Substituir `src/app/app.routes.ts`**

```typescript
import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'projects',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/projects/projects-list/projects-list.component').then(m => m.ProjectsListComponent)
  },
  {
    path: 'projects/:id/tasks',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/tasks/tasks-list/tasks-list.component').then(m => m.TasksListComponent)
  },
  {
    path: 'tasks/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/task-detail/task-detail.component').then(m => m.TaskDetailComponent)
  },
  { path: '**', redirectTo: 'projects' }
];
```

- [ ] **Passo 4: Substituir `src/app/app.config.ts`**

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: { preset: Aura, options: { darkModeSelector: false } }
    })
  ]
};
```

- [ ] **Passo 5: Commit**

```bash
git add teste-manager-frontend/src/app/core/ teste-manager-frontend/src/app/app.routes.ts teste-manager-frontend/src/app/app.config.ts
git commit -m "feat: add AuthInterceptor, AuthGuard and configure app routes"
```

---

## Task 7: Login e Register

**Files:**
- Create: `teste-manager-frontend/src/app/features/auth/login/login.component.ts`
- Create: `teste-manager-frontend/src/app/features/auth/register/register.component.ts`

- [ ] **Passo 1: Criar `src/app/features/auth/login/login.component.ts`**

```typescript
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, ButtonModule, InputTextModule, ToastModule],
  providers: [MessageService],
  template: `
    <p-toast />
    <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div class="bg-white border border-gray-200 rounded-xl p-8 w-full max-w-sm shadow-sm">
        <h1 class="text-2xl font-semibold text-gray-900 mb-1">Entrar</h1>
        <p class="text-sm text-gray-500 mb-6">Acesse sua conta para continuar</p>

        <div class="flex flex-col gap-4">
          <div class="flex flex-col gap-1">
            <label class="text-sm font-medium text-gray-700">Email</label>
            <input pInputText type="email" [(ngModel)]="email"
              placeholder="seu@email.com" class="w-full" />
          </div>
          <div class="flex flex-col gap-1">
            <label class="text-sm font-medium text-gray-700">Senha</label>
            <input pInputText type="password" [(ngModel)]="password"
              placeholder="••••••••" class="w-full" />
          </div>
          <p-button label="Entrar" [loading]="loading"
            (onClick)="onLogin()" styleClass="w-full justify-center" />
        </div>

        <p class="text-sm text-center text-gray-500 mt-6">
          Não tem conta?
          <a routerLink="/register" class="text-gray-900 font-medium hover:underline">
            Criar conta
          </a>
        </p>
      </div>
    </div>
  `
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private messageService = inject(MessageService);

  email = '';
  password = '';
  loading = false;

  onLogin() {
    if (!this.email || !this.password) return;
    this.loading = true;
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigate(['/projects']),
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Email ou senha inválidos'
        });
        this.loading = false;
      }
    });
  }
}
```

- [ ] **Passo 2: Criar `src/app/features/auth/register/register.component.ts`**

```typescript
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink, ButtonModule, InputTextModule, ToastModule],
  providers: [MessageService],
  template: `
    <p-toast />
    <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
      <div class="bg-white border border-gray-200 rounded-xl p-8 w-full max-w-sm shadow-sm">
        <h1 class="text-2xl font-semibold text-gray-900 mb-1">Criar conta</h1>
        <p class="text-sm text-gray-500 mb-6">Preencha os dados para começar</p>

        <div class="flex flex-col gap-4">
          <div class="flex flex-col gap-1">
            <label class="text-sm font-medium text-gray-700">Nome</label>
            <input pInputText type="text" [(ngModel)]="name"
              placeholder="Seu nome" class="w-full" />
          </div>
          <div class="flex flex-col gap-1">
            <label class="text-sm font-medium text-gray-700">Email</label>
            <input pInputText type="email" [(ngModel)]="email"
              placeholder="seu@email.com" class="w-full" />
          </div>
          <div class="flex flex-col gap-1">
            <label class="text-sm font-medium text-gray-700">Senha</label>
            <input pInputText type="password" [(ngModel)]="password"
              placeholder="Mínimo 6 caracteres" class="w-full" />
          </div>
          <p-button label="Criar conta" [loading]="loading"
            (onClick)="onRegister()" styleClass="w-full justify-center" />
        </div>

        <p class="text-sm text-center text-gray-500 mt-6">
          Já tem conta?
          <a routerLink="/login" class="text-gray-900 font-medium hover:underline">
            Entrar
          </a>
        </p>
      </div>
    </div>
  `
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private messageService = inject(MessageService);

  name = '';
  email = '';
  password = '';
  loading = false;

  onRegister() {
    if (!this.name || !this.email || !this.password) return;
    this.loading = true;
    this.authService.register({ name: this.name, email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigate(['/projects']),
      error: (err) => {
        const detail = err.status === 409
          ? 'Este email já está em uso'
          : 'Erro ao criar conta. Tente novamente.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail });
        this.loading = false;
      }
    });
  }
}
```

- [ ] **Passo 3: Verificar que compila**

```bash
cd teste-manager-frontend
ng build
```

Esperado: `Application bundle generation complete.`

- [ ] **Passo 4: Commit**

```bash
git add teste-manager-frontend/src/app/features/auth/
git commit -m "feat: add Login and Register components"
```

---

## Task 8: NavbarComponent

**Files:**
- Create: `teste-manager-frontend/src/app/shared/components/navbar/navbar.component.ts`

- [ ] **Passo 1: Criar `src/app/shared/components/navbar/navbar.component.ts`**

```typescript
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, ButtonModule],
  template: `
    <header class="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div class="max-w-6xl mx-auto px-6 h-14 flex items-center justify-between">
        <a routerLink="/projects" class="text-base font-semibold text-gray-900 hover:text-gray-700">
          Task Manager
        </a>
        <p-button
          label="Sair"
          severity="secondary"
          [text]="true"
          size="small"
          icon="pi pi-sign-out"
          (onClick)="logout()"
        />
      </div>
    </header>
  `
})
export class NavbarComponent {
  private authService = inject(AuthService);

  logout() {
    this.authService.logout();
  }
}
```

- [ ] **Passo 2: Commit**

```bash
git add teste-manager-frontend/src/app/shared/
git commit -m "feat: add NavbarComponent"
```

---

## Task 9: ProjectsListComponent

**Files:**
- Create: `teste-manager-frontend/src/app/features/projects/projects-list/projects-list.component.ts`

- [ ] **Passo 1: Criar `src/app/features/projects/projects-list/projects-list.component.ts`**

```typescript
import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { ProjectService } from '../../../core/services/project.service';
import { Project } from '../../../core/models/project.model';

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [
    FormsModule, ButtonModule, InputTextModule, DialogModule,
    ToastModule, TagModule, ConfirmDialogModule, NavbarComponent
  ],
  providers: [MessageService, ConfirmationService],
  template: `
    <app-navbar />
    <p-toast />
    <p-confirmDialog />

    <main class="max-w-6xl mx-auto px-6 py-8">
      <div class="flex items-center justify-between mb-6">
        <h1 class="text-2xl font-semibold text-gray-900">Projetos</h1>
        <p-button label="Novo Projeto" icon="pi pi-plus" (onClick)="openDialog()" />
      </div>

      @if (loading()) {
        <div class="flex justify-center py-16">
          <i class="pi pi-spin pi-spinner text-2xl text-gray-400"></i>
        </div>
      } @else if (projects().length === 0) {
        <div class="text-center py-16 text-gray-400">
          <i class="pi pi-folder-open text-4xl mb-3 block"></i>
          <p>Nenhum projeto ainda. Crie o primeiro!</p>
        </div>
      } @else {
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          @for (project of projects(); track project.id) {
            <div
              class="bg-white border border-gray-200 rounded-xl p-5 shadow-sm hover:shadow-md transition-shadow cursor-pointer"
              (click)="goToTasks(project.id)"
            >
              <div class="flex items-start justify-between mb-2">
                <h2 class="text-base font-semibold text-gray-900 truncate flex-1 mr-2">
                  {{ project.name }}
                </h2>
                <p-tag
                  [value]="project.status === 'ACTIVE' ? 'Ativo' : 'Arquivado'"
                  [severity]="project.status === 'ACTIVE' ? 'success' : 'secondary'"
                />
              </div>
              @if (project.description) {
                <p class="text-sm text-gray-500 mb-4 line-clamp-2">{{ project.description }}</p>
              }
              <div class="flex gap-2 mt-3" (click)="$event.stopPropagation()">
                @if (project.status === 'ACTIVE') {
                  <p-button
                    label="Arquivar"
                    severity="secondary"
                    [text]="true"
                    size="small"
                    icon="pi pi-inbox"
                    (onClick)="archive(project)"
                  />
                }
                <p-button
                  label="Deletar"
                  severity="danger"
                  [text]="true"
                  size="small"
                  icon="pi pi-trash"
                  (onClick)="confirmDelete(project)"
                />
              </div>
            </div>
          }
        </div>
      }
    </main>

    <p-dialog
      header="Novo Projeto"
      [(visible)]="dialogVisible"
      [modal]="true"
      [style]="{ width: '400px' }"
    >
      <div class="flex flex-col gap-4 pt-2">
        <div class="flex flex-col gap-1">
          <label class="text-sm font-medium text-gray-700">Nome *</label>
          <input pInputText [(ngModel)]="newName" placeholder="Nome do projeto" class="w-full" />
        </div>
        <div class="flex flex-col gap-1">
          <label class="text-sm font-medium text-gray-700">Descrição</label>
          <input pInputText [(ngModel)]="newDescription" placeholder="Descrição (opcional)" class="w-full" />
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancelar" severity="secondary" [text]="true" (onClick)="dialogVisible = false" />
        <p-button label="Criar" [loading]="saving" (onClick)="createProject()" />
      </ng-template>
    </p-dialog>
  `
})
export class ProjectsListComponent implements OnInit {
  private projectService = inject(ProjectService);
  private router = inject(Router);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  projects = signal<Project[]>([]);
  loading = signal(true);
  dialogVisible = false;
  saving = false;
  newName = '';
  newDescription = '';

  ngOnInit() {
    this.loadProjects();
  }

  loadProjects() {
    this.loading.set(true);
    this.projectService.list().subscribe({
      next: (page) => {
        this.projects.set(page.content);
        this.loading.set(false);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar projetos' });
        this.loading.set(false);
      }
    });
  }

  goToTasks(projectId: number) {
    this.router.navigate(['/projects', projectId, 'tasks']);
  }

  openDialog() {
    this.newName = '';
    this.newDescription = '';
    this.dialogVisible = true;
  }

  createProject() {
    if (!this.newName.trim()) return;
    this.saving = true;
    this.projectService.create({ name: this.newName.trim(), description: this.newDescription.trim() || undefined }).subscribe({
      next: (project) => {
        this.projects.update(list => [project, ...list]);
        this.dialogVisible = false;
        this.saving = false;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Projeto criado' });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao criar projeto' });
        this.saving = false;
      }
    });
  }

  archive(project: Project) {
    this.projectService.archive(project.id).subscribe({
      next: (updated) => {
        this.projects.update(list => list.map(p => p.id === updated.id ? updated : p));
        this.messageService.add({ severity: 'success', summary: 'Arquivado', detail: project.name });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao arquivar' });
      }
    });
  }

  confirmDelete(project: Project) {
    this.confirmationService.confirm({
      message: `Deletar "${project.name}" e todas as suas tarefas?`,
      header: 'Confirmar exclusão',
      icon: 'pi pi-trash',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.deleteProject(project)
    });
  }

  private deleteProject(project: Project) {
    this.projectService.delete(project.id).subscribe({
      next: () => {
        this.projects.update(list => list.filter(p => p.id !== project.id));
        this.messageService.add({ severity: 'success', summary: 'Deletado', detail: project.name });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao deletar' });
      }
    });
  }
}
```

- [ ] **Passo 2: Verificar que compila**

```bash
ng build
```

Esperado: `Application bundle generation complete.`

- [ ] **Passo 3: Commit**

```bash
git add teste-manager-frontend/src/app/features/projects/
git commit -m "feat: add ProjectsListComponent"
```

---

## Task 10: TasksListComponent

**Files:**
- Create: `teste-manager-frontend/src/app/features/tasks/tasks-list/tasks-list.component.ts`

- [ ] **Passo 1: Criar `src/app/features/tasks/tasks-list/tasks-list.component.ts`**

```typescript
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';
import { SelectModule } from 'primeng/select';
import { ProgressBarModule } from 'primeng/progressbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { TaskService } from '../../../core/services/task.service';
import { Task, TaskPriority, TaskStatus } from '../../../core/models/task.model';

@Component({
  selector: 'app-tasks-list',
  standalone: true,
  imports: [
    FormsModule, ButtonModule, InputTextModule, TableModule, DialogModule,
    ToastModule, TagModule, SelectModule, ProgressBarModule,
    ConfirmDialogModule, NavbarComponent
  ],
  providers: [MessageService, ConfirmationService],
  template: `
    <app-navbar />
    <p-toast />
    <p-confirmDialog />

    <main class="max-w-6xl mx-auto px-6 py-8">
      <div class="flex items-center gap-3 mb-6">
        <p-button icon="pi pi-arrow-left" severity="secondary" [text]="true" (onClick)="goBack()" />
        <h1 class="text-2xl font-semibold text-gray-900">Tarefas</h1>
        <div class="ml-auto">
          <p-button label="Nova Tarefa" icon="pi pi-plus" (onClick)="openDialog()" />
        </div>
      </div>

      <div class="flex gap-3 mb-5">
        <p-select
          [(ngModel)]="filterStatus"
          [options]="statusOptions"
          optionLabel="label"
          optionValue="value"
          placeholder="Status"
          [showClear]="true"
          (onChange)="loadTasks()"
          styleClass="w-40"
        />
        <p-select
          [(ngModel)]="filterPriority"
          [options]="priorityOptions"
          optionLabel="label"
          optionValue="value"
          placeholder="Prioridade"
          [showClear]="true"
          (onChange)="loadTasks()"
          styleClass="w-44"
        />
      </div>

      <p-table
        [value]="tasks()"
        [loading]="loading()"
        styleClass="p-datatable-sm"
        [rowHover]="true"
        (onRowSelect)="goToTask($event.data)"
        selectionMode="single"
      >
        <ng-template pTemplate="header">
          <tr>
            <th>Título</th>
            <th>Status</th>
            <th>Prioridade</th>
            <th>Vencimento</th>
            <th>Progresso</th>
            <th></th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-task>
          <tr [pSelectableRow]="task" class="cursor-pointer">
            <td class="font-medium text-gray-900">{{ task.title }}</td>
            <td>
              <p-tag [value]="statusLabel(task.status)" [severity]="statusSeverity(task.status)" />
            </td>
            <td>
              <p-tag [value]="priorityLabel(task.priority)" [severity]="prioritySeverity(task.priority)" />
            </td>
            <td class="text-sm text-gray-500">{{ task.dueDate ?? '—' }}</td>
            <td class="w-32">
              <div class="flex items-center gap-2">
                <p-progressBar [value]="task.progress" styleClass="flex-1 h-1.5" [showValue]="false" />
                <span class="text-xs text-gray-500 w-8">{{ task.progress }}%</span>
              </div>
            </td>
            <td (click)="$event.stopPropagation()">
              <p-button
                icon="pi pi-trash"
                severity="danger"
                [text]="true"
                size="small"
                (onClick)="confirmDelete(task)"
              />
            </td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr>
            <td colspan="6" class="text-center text-gray-400 py-8">Nenhuma tarefa encontrada</td>
          </tr>
        </ng-template>
      </p-table>
    </main>

    <p-dialog
      header="Nova Tarefa"
      [(visible)]="dialogVisible"
      [modal]="true"
      [style]="{ width: '440px' }"
    >
      <div class="flex flex-col gap-4 pt-2">
        <div class="flex flex-col gap-1">
          <label class="text-sm font-medium text-gray-700">Título *</label>
          <input pInputText [(ngModel)]="newTitle" placeholder="Título da tarefa" class="w-full" />
        </div>
        <div class="flex flex-col gap-1">
          <label class="text-sm font-medium text-gray-700">Prioridade *</label>
          <p-select
            [(ngModel)]="newPriority"
            [options]="priorityOptions"
            optionLabel="label"
            optionValue="value"
            placeholder="Selecione"
            styleClass="w-full"
          />
        </div>
        <div class="flex flex-col gap-1">
          <label class="text-sm font-medium text-gray-700">Data de vencimento</label>
          <input pInputText type="date" [(ngModel)]="newDueDate" class="w-full" />
        </div>
      </div>
      <ng-template pTemplate="footer">
        <p-button label="Cancelar" severity="secondary" [text]="true" (onClick)="dialogVisible = false" />
        <p-button label="Criar" [loading]="saving" (onClick)="createTask()" />
      </ng-template>
    </p-dialog>
  `
})
export class TasksListComponent implements OnInit {
  private taskService = inject(TaskService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  tasks = signal<Task[]>([]);
  loading = signal(true);
  projectId!: number;

  filterStatus: TaskStatus | null = null;
  filterPriority: TaskPriority | null = null;

  dialogVisible = false;
  saving = false;
  newTitle = '';
  newPriority: TaskPriority = 'MEDIUM';
  newDueDate = '';

  statusOptions = [
    { label: 'A fazer', value: 'TODO' },
    { label: 'Em andamento', value: 'IN_PROGRESS' },
    { label: 'Concluída', value: 'DONE' },
    { label: 'Cancelada', value: 'CANCELLED' }
  ];

  priorityOptions = [
    { label: 'Baixa', value: 'LOW' },
    { label: 'Média', value: 'MEDIUM' },
    { label: 'Alta', value: 'HIGH' },
    { label: 'Urgente', value: 'URGENT' }
  ];

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTasks();
  }

  loadTasks() {
    this.loading.set(true);
    this.taskService.listByProject(this.projectId, {
      status: this.filterStatus ?? undefined,
      priority: this.filterPriority ?? undefined
    }).subscribe({
      next: (page) => {
        this.tasks.set(page.content);
        this.loading.set(false);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar tarefas' });
        this.loading.set(false);
      }
    });
  }

  goBack() {
    this.router.navigate(['/projects']);
  }

  goToTask(task: Task) {
    this.router.navigate(['/tasks', task.id]);
  }

  openDialog() {
    this.newTitle = '';
    this.newPriority = 'MEDIUM';
    this.newDueDate = '';
    this.dialogVisible = true;
  }

  createTask() {
    if (!this.newTitle.trim()) return;
    this.saving = true;
    this.taskService.create(this.projectId, {
      title: this.newTitle.trim(),
      priority: this.newPriority,
      dueDate: this.newDueDate || undefined
    }).subscribe({
      next: (task) => {
        this.tasks.update(list => [task, ...list]);
        this.dialogVisible = false;
        this.saving = false;
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Tarefa criada' });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao criar tarefa' });
        this.saving = false;
      }
    });
  }

  confirmDelete(task: Task) {
    this.confirmationService.confirm({
      message: `Deletar "${task.title}"?`,
      header: 'Confirmar exclusão',
      icon: 'pi pi-trash',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.deleteTask(task)
    });
  }

  private deleteTask(task: Task) {
    this.taskService.delete(task.id).subscribe({
      next: () => {
        this.tasks.update(list => list.filter(t => t.id !== task.id));
        this.messageService.add({ severity: 'success', summary: 'Deletada', detail: task.title });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao deletar' });
      }
    });
  }

  statusLabel(status: TaskStatus): string {
    const map: Record<TaskStatus, string> = {
      TODO: 'A fazer', IN_PROGRESS: 'Em andamento', DONE: 'Concluída', CANCELLED: 'Cancelada'
    };
    return map[status];
  }

  statusSeverity(status: TaskStatus): string {
    const map: Record<TaskStatus, string> = {
      TODO: 'secondary', IN_PROGRESS: 'info', DONE: 'success', CANCELLED: 'danger'
    };
    return map[status];
  }

  priorityLabel(priority: TaskPriority): string {
    const map: Record<TaskPriority, string> = {
      LOW: 'Baixa', MEDIUM: 'Média', HIGH: 'Alta', URGENT: 'Urgente'
    };
    return map[priority];
  }

  prioritySeverity(priority: TaskPriority): string {
    const map: Record<TaskPriority, string> = {
      LOW: 'secondary', MEDIUM: 'info', HIGH: 'warn', URGENT: 'danger'
    };
    return map[priority];
  }
}
```

- [ ] **Passo 2: Verificar que compila**

```bash
ng build
```

- [ ] **Passo 3: Commit**

```bash
git add teste-manager-frontend/src/app/features/tasks/
git commit -m "feat: add TasksListComponent"
```

---

## Task 11: TaskDetailComponent

**Files:**
- Create: `teste-manager-frontend/src/app/features/task-detail/task-detail.component.ts`

- [ ] **Passo 1: Criar `src/app/features/task-detail/task-detail.component.ts`**

```typescript
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';
import { ProgressBarModule } from 'primeng/progressbar';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/api';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { TaskService } from '../../core/services/task.service';
import { SubtaskService } from '../../core/services/subtask.service';
import { Task, TaskPriority, TaskStatus } from '../../core/models/task.model';
import { Subtask } from '../../core/models/subtask.model';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [
    FormsModule, ButtonModule, InputTextModule, ToastModule,
    TagModule, ProgressBarModule, CheckboxModule, NavbarComponent
  ],
  providers: [MessageService],
  template: `
    <app-navbar />
    <p-toast />

    <main class="max-w-3xl mx-auto px-6 py-8">
      @if (loading()) {
        <div class="flex justify-center py-16">
          <i class="pi pi-spin pi-spinner text-2xl text-gray-400"></i>
        </div>
      } @else if (task()) {
        <div class="flex items-center gap-3 mb-6">
          <p-button icon="pi pi-arrow-left" severity="secondary" [text]="true" (onClick)="goBack()" />
          <div class="flex-1">
            <h1 class="text-2xl font-semibold text-gray-900">{{ task()!.title }}</h1>
          </div>
          <p-tag [value]="statusLabel(task()!.status)" [severity]="statusSeverity(task()!.status)" />
          <p-tag [value]="priorityLabel(task()!.priority)" [severity]="prioritySeverity(task()!.priority)" />
        </div>

        @if (task()!.description) {
          <p class="text-gray-600 mb-6">{{ task()!.description }}</p>
        }

        <div class="bg-white border border-gray-200 rounded-xl p-6 mb-6">
          <div class="flex items-center justify-between mb-3">
            <h2 class="text-sm font-semibold text-gray-700">Progresso</h2>
            <span class="text-sm font-medium text-gray-900">{{ task()!.progress }}%</span>
          </div>
          <p-progressBar [value]="task()!.progress" [showValue]="false" styleClass="h-2" />
          <p class="text-xs text-gray-400 mt-2">
            {{ completedCount() }} de {{ subtasks().length }} subtarefas concluídas
          </p>
        </div>

        <div class="bg-white border border-gray-200 rounded-xl p-6">
          <h2 class="text-base font-semibold text-gray-900 mb-4">Subtarefas</h2>

          <div class="flex gap-2 mb-5">
            <input
              pInputText
              [(ngModel)]="newSubtaskTitle"
              placeholder="Nova subtarefa..."
              class="flex-1"
              (keyup.enter)="addSubtask()"
            />
            <p-button icon="pi pi-plus" (onClick)="addSubtask()" [loading]="adding" />
          </div>

          @if (subtasks().length === 0) {
            <p class="text-sm text-gray-400 text-center py-4">Nenhuma subtarefa ainda</p>
          } @else {
            <ul class="flex flex-col gap-2">
              @for (subtask of subtasks(); track subtask.id) {
                <li class="flex items-center gap-3 py-2 border-b border-gray-100 last:border-0">
                  <p-checkbox
                    [binary]="true"
                    [ngModel]="subtask.completed"
                    [disabled]="subtask.completed"
                    (onChange)="completeSubtask(subtask)"
                  />
                  <span class="flex-1 text-sm"
                    [class.line-through]="subtask.completed"
                    [class.text-gray-400]="subtask.completed"
                    [class.text-gray-800]="!subtask.completed">
                    {{ subtask.title }}
                  </span>
                  <p-button
                    icon="pi pi-trash"
                    severity="danger"
                    [text]="true"
                    size="small"
                    (onClick)="deleteSubtask(subtask)"
                  />
                </li>
              }
            </ul>
          }
        </div>
      }
    </main>
  `
})
export class TaskDetailComponent implements OnInit {
  private taskService = inject(TaskService);
  private subtaskService = inject(SubtaskService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private messageService = inject(MessageService);

  task = signal<Task | null>(null);
  subtasks = signal<Subtask[]>([]);
  loading = signal(true);
  adding = false;
  newSubtaskTitle = '';

  completedCount = () => this.subtasks().filter(s => s.completed).length;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTask(id);
    this.loadSubtasks(id);
  }

  loadTask(id: number) {
    this.taskService.getById(id).subscribe({
      next: (task) => {
        this.task.set(task);
        this.loading.set(false);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Tarefa não encontrada' });
        this.loading.set(false);
      }
    });
  }

  loadSubtasks(id: number) {
    this.subtaskService.listByTask(id).subscribe({
      next: (subtasks) => this.subtasks.set(subtasks),
      error: () => {}
    });
  }

  goBack() {
    const projectId = this.task()?.projectId;
    if (projectId) this.router.navigate(['/projects', projectId, 'tasks']);
    else this.router.navigate(['/projects']);
  }

  addSubtask() {
    if (!this.newSubtaskTitle.trim() || !this.task()) return;
    this.adding = true;
    this.subtaskService.create(this.task()!.id, { title: this.newSubtaskTitle.trim() }).subscribe({
      next: (subtask) => {
        this.subtasks.update(list => [...list, subtask]);
        this.newSubtaskTitle = '';
        this.adding = false;
        this.refreshProgress();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao criar subtarefa' });
        this.adding = false;
      }
    });
  }

  completeSubtask(subtask: Subtask) {
    if (subtask.completed) return;
    this.subtaskService.complete(subtask.id).subscribe({
      next: (updated) => {
        this.subtasks.update(list => list.map(s => s.id === updated.id ? updated : s));
        this.refreshProgress();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao concluir subtarefa' });
      }
    });
  }

  deleteSubtask(subtask: Subtask) {
    this.subtaskService.delete(subtask.id).subscribe({
      next: () => {
        this.subtasks.update(list => list.filter(s => s.id !== subtask.id));
        this.refreshProgress();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao deletar subtarefa' });
      }
    });
  }

  private refreshProgress() {
    if (!this.task()) return;
    this.taskService.getById(this.task()!.id).subscribe({
      next: (updated) => this.task.set(updated)
    });
  }

  statusLabel(status: TaskStatus): string {
    const map: Record<TaskStatus, string> = {
      TODO: 'A fazer', IN_PROGRESS: 'Em andamento', DONE: 'Concluída', CANCELLED: 'Cancelada'
    };
    return map[status];
  }

  statusSeverity(status: TaskStatus): string {
    const map: Record<TaskStatus, string> = {
      TODO: 'secondary', IN_PROGRESS: 'info', DONE: 'success', CANCELLED: 'danger'
    };
    return map[status];
  }

  priorityLabel(priority: TaskPriority): string {
    const map: Record<TaskPriority, string> = {
      LOW: 'Baixa', MEDIUM: 'Média', HIGH: 'Alta', URGENT: 'Urgente'
    };
    return map[priority];
  }

  prioritySeverity(priority: TaskPriority): string {
    const map: Record<TaskPriority, string> = {
      LOW: 'secondary', MEDIUM: 'info', HIGH: 'warn', URGENT: 'danger'
    };
    return map[priority];
  }
}
```

- [ ] **Passo 2: Verificar que compila**

```bash
ng build
```

Esperado: `Application bundle generation complete.`

- [ ] **Passo 3: Testar manualmente**

Com a API rodando (`./mvnw spring-boot:run` no backend):

```bash
ng serve
```

Abrir `http://localhost:4200`, criar conta, criar projeto, criar tarefa, criar subtarefa, concluir subtarefa — verificar que o progresso atualiza.

- [ ] **Passo 4: Commit final**

```bash
git add teste-manager-frontend/src/app/features/task-detail/
git commit -m "feat: add TaskDetailComponent with subtasks and progress"
git push
```
