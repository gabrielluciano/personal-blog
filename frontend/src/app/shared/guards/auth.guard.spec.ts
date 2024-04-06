import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { AppState } from '../state/app.state';
import { initialState } from '../state/auth/auth.reducer';
import { editorGuard } from './auth.guard';

describe('editorGuard', () => {
  let store: MockStore<AppState>;
  let router: Router;
  const initialAppState: AppState = { auth: initialState };

  const executeGuard = () => TestBed.runInInjectionContext(() => editorGuard());

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideMockStore({ initialState: initialAppState }),
      ],
    }).compileComponents();

    store = TestBed.inject(MockStore);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should return an Observable of false when user is not authenticated', () => {
    const routerNavigateSpy = spyOn(router, 'navigate');
    executeGuard().subscribe((value) => {
      expect(value).toBeFalsy();
    });
    expect(routerNavigateSpy).toHaveBeenCalledOnceWith(['/']);
  });

  it('should return an Observable of false when user is authenticated but is not an editor', () => {
    store.setState({
      auth: {
        isAuthenticated: true,
        isEditor: false,
        isAdmin: false,
      },
    });
    const routerNavigateSpy = spyOn(router, 'navigate');
    executeGuard().subscribe((value) => {
      expect(value).toBeFalsy();
    });
    expect(routerNavigateSpy).toHaveBeenCalledOnceWith(['/']);
  });

  it('should return an Observable of true when user is authenticated and is an editor', () => {
    store.setState({
      auth: {
        isAuthenticated: true,
        isEditor: true,
        isAdmin: false,
      },
    });
    executeGuard().subscribe((value) => {
      expect(value).toBeTruthy();
    });
  });
});
