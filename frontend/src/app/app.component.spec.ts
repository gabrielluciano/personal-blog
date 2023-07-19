import { TestBed, ComponentFixture } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { JwtModule } from '@auth0/angular-jwt';
import { environment as env } from 'src/environments/environment';
import { AuthService } from './shared/services/auth.service';
import { AppState } from './shared/state/app.state';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { initialState } from './shared/state/auth/auth.reducer';
import { JwtToken } from './models/jwtToken';
import { authenticate } from './shared/state/auth/auth.actions';

function tokenGetter() {
  return localStorage.getItem('access_token');
}

describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let store: MockStore<AppState>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  const initialAppState: AppState = { auth: initialState };

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['getDecodedToken']);

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        SharedModule,
        JwtModule.forRoot({
          config: {
            tokenGetter,
            allowedDomains: env.angularJwtAllowedDomains,
          },
        }),
      ],
      declarations: [AppComponent],
      providers: [
        provideMockStore({ initialState: initialAppState }),
        { provide: AuthService, useValue: authServiceSpy },
      ],
    });
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
