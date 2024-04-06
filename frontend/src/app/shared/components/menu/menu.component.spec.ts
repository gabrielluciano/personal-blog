import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MockStore, provideMockStore } from '@ngrx/store/testing';

import { AuthService } from '../../services/auth.service';
import { AppState } from '../../state/app.state';
import { logout } from '../../state/auth/auth.actions';
import { initialState } from '../../state/auth/auth.reducer';
import { MenuComponent } from './menu.component';

describe('MenuComponent', () => {
  let component: MenuComponent;
  let fixture: ComponentFixture<MenuComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let store: MockStore<AppState>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let snackBarSpy: jasmine.SpyObj<MatSnackBar>;
  const initialAppState: AppState = { auth: initialState };

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['logout']);
    dialogSpy = jasmine.createSpyObj<MatDialog>('MatDialog', ['open']);
    snackBarSpy = jasmine.createSpyObj<MatSnackBar>('MatSnackBar', ['openFromComponent']);

    await TestBed.configureTestingModule({
      imports: [MenuComponent],
      providers: [
        provideMockStore({ initialState: initialAppState }),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: MatSnackBar, useValue: snackBarSpy },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(MenuComponent);
    component = fixture.componentInstance;
    store = TestBed.inject(MockStore);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call closeMenu when close menu button is clicked', fakeAsync(() => {
    spyOn(component, 'closeMenu');
    const button = fixture.debugElement.nativeElement.querySelector(
      'button[aria-label="Fechar menu"]',
    );

    button.click();
    tick();

    expect(component.closeMenu).toHaveBeenCalled();
  }));

  it('should call mat dialog open method when login button is clicked', fakeAsync(() => {
    const button = fixture.debugElement.nativeElement.querySelector('button[aria-label="Login"]');

    button.click();
    tick();

    expect(dialogSpy.open).toHaveBeenCalled();
  }));

  it('should call logout method when user is authenticated and logout button is clicked', fakeAsync(() => {
    const storeDispatchSpy = spyOn(store, 'dispatch');
    store.setState({
      auth: {
        ...initialAppState.auth,
        isAuthenticated: true,
      },
    });
    tick();
    fixture.detectChanges();

    const button = fixture.debugElement.nativeElement.querySelector('button[aria-label="Logout"]');
    button.click();
    tick();

    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(storeDispatchSpy).toHaveBeenCalledWith(logout());
    expect(snackBarSpy.openFromComponent).toHaveBeenCalled();
  }));
});
