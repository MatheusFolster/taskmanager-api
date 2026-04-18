import { Component } from '@angular/core';

interface Feature {
  title: string;
  description: string;
  icon: string;
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
