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
