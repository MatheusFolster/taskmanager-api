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
    { category: 'Backend', items: ['Java 21', 'Spring Boot 3.4', 'Spring Security', 'JPA / Hibernate', 'Flyway'] },
    { category: 'Banco de Dados', items: ['PostgreSQL', 'H2 (testes)'] },
    { category: 'Testes', items: ['JUnit 5', 'Mockito', 'MockMvc'] },
    { category: 'Documentação', items: ['Swagger / OpenAPI 3', 'MapStruct'] },
    { category: 'Frontend', items: ['Angular 21', 'Tailwind CSS', 'highlight.js'] },
    { category: 'DevOps', items: ['Docker Compose', 'GitHub Actions'] },
  ];
}
