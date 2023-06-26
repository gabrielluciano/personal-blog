import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginFormComponent } from './login-form.component';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { HeaderComponent } from '../header/header.component';
import { ErrorDetails } from 'src/app/models/errorDetails';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;

  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<HeaderComponent>>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;

  const errorDetailsMock: Partial<ErrorDetails> = {
    message: 'Ops we had an error!',
  };

  beforeEach(() => {
    localStorage.clear();

    dialogRefSpy = jasmine.createSpyObj<MatDialogRef<HeaderComponent>>('MatDialogRef', ['close']);
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['login']);
    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);

    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule, MatDialogModule, MatInputModule, ReactiveFormsModule],
      declarations: [LoginFormComponent],
      providers: [
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ],
    });
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close the dialog when close button is clicked', () => {
    const closeButton = fixture.elementRef.nativeElement.querySelector('button[type="reset"]');
    closeButton.click();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });

  it('should call set token to localStorage and call dialogRef method when autthentication is successful', () => {
    authServiceSpy.login.and.returnValue(of('token'));
    component.onSubmit();
    expect(dialogRefSpy.close).toHaveBeenCalled();
    expect(localStorage.getItem('access_token')).toEqual('token');
  });

  it('should call snackBar when authService login method return an error', () => {
    authServiceSpy.login.and.returnValue(throwError(() => errorDetailsMock));
    component.onSubmit();
    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
  });
});
