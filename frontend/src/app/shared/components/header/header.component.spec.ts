import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { MatIcon } from '@angular/material/icon';
import { provideRouter } from '@angular/router';
import { routes } from 'src/app/app.routes';
import { MenuComponent } from '../menu/menu.component';
import { HeaderComponent } from './header.component';

@Component({
  selector: 'app-menu',
  template: '<button id="close-button" (click)="closeMenu()">close</button>',
  standalone: true,
  imports: [MatIcon],
})
class MockMenuComponent {
  @Input() show: boolean = true;
  @Output() whenClose: EventEmitter<void> = new EventEmitter();

  closeMenu() {
    this.whenClose.emit();
  }
}

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [provideRouter(routes)],
    })
      .overrideComponent(HeaderComponent, {
        remove: { imports: [MenuComponent] },
        add: { imports: [MockMenuComponent] },
      })
      .compileComponents();
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
