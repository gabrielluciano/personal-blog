import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { AppState } from '../../state/app.state';
import { initialState } from '../../state/auth/auth.reducer';
import { MatSnackBar } from '@angular/material/snack-bar';
import { logout } from '../../state/auth/auth.actions';
import { MatIconModule } from '@angular/material/icon';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let store: MockStore<AppState>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  const initialAppState: AppState = { auth: initialState };

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['logout']);
    dialogSpy = jasmine.createSpyObj<MatDialog>('MatDialog', ['open']);
    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);

    TestBed.configureTestingModule({
      imports: [MatIconModule],
      declarations: [HeaderComponent],
      providers: [
        provideMockStore({ initialState: initialAppState }),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ],
    });
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call mat dialog open method', fakeAsync(() => {
    const button = fixture.debugElement.nativeElement.querySelector('.button');
    button.click();
    tick();
    expect(dialogSpy.open).toHaveBeenCalled();
  }));

  it('should call logout method when user is authenticated', fakeAsync(() => {
    const storeDispatchSpy = spyOn(store, 'dispatch');
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
      },
    });
    tick();
    fixture.detectChanges();
    const button = fixture.debugElement.nativeElement.querySelector('.button');
    button.click();
    tick();
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(storeDispatchSpy).toHaveBeenCalledWith(logout());
    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
  }));
});
