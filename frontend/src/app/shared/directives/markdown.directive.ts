import { Directive, ElementRef, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import hljs from 'highlight.js/lib/core';
import java from 'highlight.js/lib/languages/java';
import { marked } from 'marked';

@Directive({
  selector: '[appMarkdown]',
})
export class MarkdownDirective implements OnInit {
  @Input() appMarkdown = '';

  constructor(private el: ElementRef, private domSanitizer: DomSanitizer) {
    hljs.registerLanguage('java', java);
  }

  ngOnInit(): void {
    this.el.nativeElement.innerHTML = this.domSanitizer.sanitize(1, marked.parse(this.appMarkdown));
    this.el.nativeElement.querySelectorAll('pre code').forEach(hljs.highlightElement);
  }
}
