import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import hljs from 'highlight.js/lib/core';
import http from 'highlight.js/lib/languages/http';

hljs.registerLanguage('http', http);

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
