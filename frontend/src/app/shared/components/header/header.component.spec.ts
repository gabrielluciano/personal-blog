import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-menu',
  template: '<button id="close-button" (click)="closeMenu()">close</button>',
})
class MockAppMenuComponent {
  @Input() show: boolean = true;
  @Output() whenClose: EventEmitter<void> = new EventEmitter();

  closeMenu() {
    this.whenClose.emit();
  }
}

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatIconModule],
      declarations: [HeaderComponent, MockAppMenuComponent],
    });
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call openMenu method', fakeAsync(() => {
    spyOn(component, 'openMenu');
    const button = fixture.debugElement.nativeElement.querySelector(
      'button[aria-label="Abrir menu"]',
    );

    button.click();
    tick();

    expect(component.openMenu).toHaveBeenCalled();
  }));

  it('should call closeMenu method when app menu close button is clicked', fakeAsync(() => {
    spyOn(component, 'closeMenu');
    const button = fixture.debugElement.nativeElement.querySelector('button#close-button');

    button.click();
    tick();

    expect(component.closeMenu).toHaveBeenCalled();
  }));
});
