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
