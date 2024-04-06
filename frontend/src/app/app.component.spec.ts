import { importProvidersFrom } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { JwtModule } from '@auth0/angular-jwt';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { environment as env } from 'src/environments/environment';
import { AppComponent } from './app.component';
import { routes } from './app.routes';
import { JwtToken } from './models/jwtToken';
import { AuthService } from './shared/services/auth.service';
import { AppState } from './shared/state/app.state';
import { authenticate } from './shared/state/auth/auth.actions';
import { initialState } from './shared/state/auth/auth.reducer';

function tokenGetter() {
  return localStorage.getItem('access_token');
}

describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let store: MockStore<AppState>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  const initialAppState: AppState = { auth: initialState };

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['getDecodedToken']);

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        provideRouter(routes),
        provideMockStore({ initialState: initialAppState }),
        { provide: AuthService, useValue: authServiceSpy },
        importProvidersFrom(
          JwtModule.forRoot({
            config: {
              tokenGetter,
              allowedDomains: env.angularJwtAllowedDomains,
            },
          }),
        ),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    store = TestBed.inject(MockStore);
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should dispatch authenticate action when user is authenticated', async () => {
    const decodedToken: JwtToken = {
      email: 'email@email.com',
      iat: Date.now() / 1000,
      exp: Date.now() / 1000 + 600,
      id: 1,
      roles: ['USER', 'EDITOR', 'ADMIN'],
    };
    authServiceSpy.getDecodedToken.and.returnValue(Promise.resolve(decodedToken));
    const storeDispatchSpy = spyOn(store, 'dispatch');

    app.ngOnInit();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(storeDispatchSpy).toHaveBeenCalledWith(authenticate({ token: decodedToken }));
  });

  it('should do nothing when user is not authenticated', async () => {
    authServiceSpy.getDecodedToken.and.returnValue(Promise.resolve(null));
    const storeDispatchSpy = spyOn(store, 'dispatch');

    app.ngOnInit();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(storeDispatchSpy).toHaveBeenCalledTimes(0);
  });
});
