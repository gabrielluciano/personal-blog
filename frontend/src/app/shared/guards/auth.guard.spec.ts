import { TestBed } from '@angular/core/testing';

import { authGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

describe('authGuard', () => {
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  const executeGuard = () => TestBed.runInInjectionContext(() => authGuard());

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['isEditor']);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [{ provide: AuthService, useValue: authServiceSpy }],
    });

    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should return true when user is an editor', async () => {
    authServiceSpy.isEditor.and.returnValue(Promise.resolve(true));
    await expectAsync(executeGuard()).toBeResolvedTo(true);
  });

  it('should return true when user is an editor', async () => {
    const routerNavigateSpy = spyOn(router, 'navigate');
    authServiceSpy.isEditor.and.returnValue(Promise.resolve(false));

    await expectAsync(executeGuard()).toBeResolvedTo(false);
    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);
  });
});
