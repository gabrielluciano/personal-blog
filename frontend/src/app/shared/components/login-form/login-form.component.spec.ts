import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { LoginFormComponent } from './login-form.component';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthService } from '../../services/auth.service';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  const matDialogRefMock = {
    close: () => console.log('close method was called'),
  };

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['login']);

    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, MatDialogModule, MatInputModule, ReactiveFormsModule],
      declarations: [LoginFormComponent],
      providers: [
        { provide: MatDialogRef, useValue: matDialogRefMock },
        { provide: AuthService, useValue: authServiceSpy },
      ],
    });
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close the dialog when close button is clicked', fakeAsync(() => {
    spyOn(component.dialogRef, 'close');

    const closeButton = fixture.elementRef.nativeElement.querySelector('button[type="reset"]');
    closeButton.click();

    tick();

    expect(component.dialogRef.close).toHaveBeenCalled();
  }));
});
