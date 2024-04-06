import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import hljs from 'highlight.js/lib/core';
import { marked } from 'marked';

import bash from 'highlight.js/lib/languages/bash';
import css from 'highlight.js/lib/languages/css';
import dockerfile from 'highlight.js/lib/languages/dockerfile';
import java from 'highlight.js/lib/languages/java';
import javascript from 'highlight.js/lib/languages/javascript';
import json from 'highlight.js/lib/languages/json';
import pgsql from 'highlight.js/lib/languages/pgsql';
import plaintext from 'highlight.js/lib/languages/plaintext';
import powershell from 'highlight.js/lib/languages/powershell';
import python from 'highlight.js/lib/languages/python';
import scss from 'highlight.js/lib/languages/scss';
import shell from 'highlight.js/lib/languages/shell';
import sql from 'highlight.js/lib/languages/sql';
import typescript from 'highlight.js/lib/languages/typescript';
import xml from 'highlight.js/lib/languages/xml';
import yaml from 'highlight.js/lib/languages/yaml';

@Directive({
  selector: '[appMarkdown]',
  standalone: true,
})
export class MarkdownDirective implements OnInit {
  @Input() appMarkdown = '';

  constructor(
    private el: ElementRef,
    private domSanitizer: DomSanitizer,
  ) {
    this.registerLanguages();
  }

  ngOnInit(): void {
    this.el.nativeElement.innerHTML = this.domSanitizer.sanitize(1, marked.parse(this.appMarkdown));
    this.el.nativeElement.querySelectorAll('pre code').forEach(hljs.highlightElement);
  }

  private registerLanguages() {
    hljs.registerLanguage('plaintext', plaintext);
    hljs.registerLanguage('xml', xml);
    hljs.registerLanguage('css', css);
    hljs.registerLanguage('scss', scss);
    hljs.registerLanguage('java', java);
    hljs.registerLanguage('javascript', javascript);
    hljs.registerLanguage('python', python);
    hljs.registerLanguage('typescript', typescript);
    hljs.registerLanguage('sql', sql);
    hljs.registerLanguage('pgsql', pgsql);
    hljs.registerLanguage('shell', shell);
    hljs.registerLanguage('bash', bash);
    hljs.registerLanguage('powershell', powershell);
    hljs.registerLanguage('json', json);
    hljs.registerLanguage('yaml', yaml);
    hljs.registerLanguage('dockerfile', dockerfile);
  }
}
