import { Component, DebugElement } from '@angular/core';
import { MarkdownDirective } from './markdown.directive';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

@Component({
  template: `
    <div>
      <main [appMarkdown]="markdown"></main>
    </div>
  `,
})
class TestComponent {
  markdown =
    '## A H2 title.\nA paragraph with **strong** text and *italic* text.\n```java\nString text = "Hello, World!";\n```';
}

describe('HighlightDirective', () => {
  let fixture: ComponentFixture<TestComponent>;
  let des: DebugElement[];

  beforeEach(() => {
    fixture = TestBed.configureTestingModule({
      declarations: [MarkdownDirective, TestComponent],
    }).createComponent(TestComponent);

    des = fixture.debugElement.queryAll(By.directive(MarkdownDirective));
  });

  it('should create an instance', () => {
    expect(des.length).toBe(1);
  });

  it('should convert the markdown to html', () => {
    fixture.detectChanges();
    const mainTag = des[0].nativeElement;

    expect(mainTag.querySelectorAll('p').length).toBe(1);
    expect(mainTag.querySelectorAll('h2').length).toBe(1);
    expect(mainTag.querySelectorAll('pre code.language-java').length).toBe(1);
  });

  it('should highlight the code with highlight.js', () => {
    fixture.detectChanges();
    const mainTag = des[0].nativeElement;

    expect(
      mainTag.querySelectorAll('pre code.language-java')[0].classList.contains('hljs')
    ).toBeTruthy();
  });
});
