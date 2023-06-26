import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { MatDialogModule } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', [
      'isAuthenticated',
      'logout',
    ]);

    TestBed.configureTestingModule({
      imports: [MatDialogModule],
      declarations: [HeaderComponent],
      providers: [{ provide: AuthService, useValue: authServiceSpy }],
    });
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call mat dialog open method', fakeAsync(() => {
    spyOn(component.dialog, 'open');

    const button = fixture.debugElement.nativeElement.querySelector('.button');
    button.click();

    tick();

    expect(component.dialog.open).toHaveBeenCalled();
  }));
});
