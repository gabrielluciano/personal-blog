import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { provideAnimations } from '@angular/platform-browser/animations';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { of, throwError } from 'rxjs';
import { ErrorDetails } from 'src/app/models/errorDetails';
import { JwtToken } from 'src/app/models/jwtToken';
import { AuthService } from '../../services/auth.service';
import { StorageService } from '../../services/storage.service';
import { AppState } from '../../state/app.state';
import { login } from '../../state/auth/auth.actions';
import { initialState } from '../../state/auth/auth.reducer';
import { HeaderComponent } from '../header/header.component';
import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;

  let store: MockStore<AppState>;

  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<HeaderComponent>>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  let storageServiceSpy: jasmine.SpyObj<StorageService>;

  const initialAppState: AppState = { auth: initialState };
  const errorDetailsMock: Partial<ErrorDetails> = {
    message: 'Ops we had an error!',
  };

  beforeEach(async () => {
    localStorage.clear();

    dialogRefSpy = jasmine.createSpyObj<MatDialogRef<HeaderComponent>>('MatDialogRef', ['close']);
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['login', 'getDecodedToken']);
    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);
    storageServiceSpy = jasmine.createSpyObj<StorageService>('StorageService', ['setItem']);

    await TestBed.configureTestingModule({
      imports: [LoginFormComponent],
      providers: [
        provideAnimations(),
        provideMockStore({ initialState: initialAppState }),
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
        { provide: StorageService, useValue: storageServiceSpy },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
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

  it('should set token to localStorage, dispatch login action, show snack bar and close dialog when autthentication is successful', async () => {
    const decodedToken: JwtToken = {
      email: 'email@email.com',
      iat: Date.now() / 1000,
      exp: Date.now() / 1000 + 600,
      id: 1,
      roles: ['USER', 'EDITOR', 'ADMIN'],
    };
    authServiceSpy.login.and.returnValue(of('token'));
    authServiceSpy.getDecodedToken.and.returnValue(Promise.resolve(decodedToken));

    const storeDispatchSpy = spyOn(store, 'dispatch');

    component.onSubmit();
    await fixture.whenStable();

    expect(storageServiceSpy.setItem).toHaveBeenCalledWith('access_token', 'token');
    expect(storeDispatchSpy).toHaveBeenCalledWith(login({ token: decodedToken }));
    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });

  it('should call snackBar when authService login method return an error', async () => {
    authServiceSpy.login.and.returnValue(throwError(() => errorDetailsMock));

    component.onSubmit();
    await fixture.whenStable();

    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
  });
});
