import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { marked } from 'marked';
import hljs from 'highlight.js/lib/core';

import plaintext from 'highlight.js/lib/languages/plaintext';
import xml from 'highlight.js/lib/languages/xml';
import css from 'highlight.js/lib/languages/css';
import scss from 'highlight.js/lib/languages/scss';
import java from 'highlight.js/lib/languages/java';
import javascript from 'highlight.js/lib/languages/javascript';
import python from 'highlight.js/lib/languages/python';
import typescript from 'highlight.js/lib/languages/typescript';
import sql from 'highlight.js/lib/languages/sql';
import pgsql from 'highlight.js/lib/languages/pgsql';
import shell from 'highlight.js/lib/languages/shell';
import bash from 'highlight.js/lib/languages/bash';
import powershell from 'highlight.js/lib/languages/powershell';
import json from 'highlight.js/lib/languages/json';
import yaml from 'highlight.js/lib/languages/yaml';
import dockerfile from 'highlight.js/lib/languages/dockerfile';

@Directive({
  selector: '[appMarkdown]',
})
export class MarkdownDirective implements OnInit {
  @Input() appMarkdown = '';

  constructor(private el: ElementRef, private domSanitizer: DomSanitizer) {
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
